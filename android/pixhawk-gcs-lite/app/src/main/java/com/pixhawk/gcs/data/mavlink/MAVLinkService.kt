package com.pixhawk.gcs.data.mavlink

import io.dronefleet.mavlink.MavlinkMessage
import io.dronefleet.mavlink.common.*
import io.dronefleet.mavlink.util.MavlinkMessageReader
import io.dronefleet.mavlink.util.MavlinkMessageWriter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import com.pixhawk.gcs.data.connection.ConnectionManager
import com.pixhawk.gcs.domain.model.VehicleState
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import javax.inject.Inject
import javax.inject.Singleton

interface MAVLinkService {
    val vehicleState: StateFlow<VehicleState>
    
    suspend fun sendCommand(command: MavCmd, param1: Float = 0f, param2: Float = 0f, 
                           param3: Float = 0f, param4: Float = 0f, param5: Float = 0f, 
                           param6: Float = 0f, param7: Float = 0f): Result<Unit>
    suspend fun sendHeartbeat()
    suspend fun requestParameterList()
    suspend fun requestMissionList()
    suspend fun setParameter(paramId: String, value: Float): Result<Unit>
    suspend fun setMode(baseMode: Int, customMode: Long): Result<Unit>
}

@Singleton
class MAVLinkServiceImpl @Inject constructor(
    private val connectionManager: ConnectionManager
) : MAVLinkService {
    
    private val _vehicleState = MutableStateFlow(VehicleState())
    override val vehicleState: StateFlow<VehicleState> = _vehicleState.asStateFlow()
    
    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var messageReader: MavlinkMessageReader? = null
    private var messageWriter: MavlinkMessageWriter? = null
    private var dataProcessingJob: Job? = null
    
    private val gcsSystemId = 255
    private val gcsComponentId = 190
    
    init {
        messageReader = MavlinkMessageReader()
        messageWriter = MavlinkMessageWriter()
        
        // Start processing incoming data
        startDataProcessing()
    }
    
    private fun startDataProcessing() {
        dataProcessingJob = connectionManager.incomingData
            .onEach { data ->
                if (data.isNotEmpty()) {
                    processIncomingData(data)
                }
            }
            .launchIn(coroutineScope)
    }
    
    private suspend fun processIncomingData(data: ByteArray) {
        try {
            val inputStream = ByteArrayInputStream(data)
            val messages = messageReader?.read(inputStream) ?: return
            
            for (message in messages) {
                when (message.payload) {
                    is Heartbeat -> handleHeartbeat(message.payload as Heartbeat, message.originSystemId, message.originComponentId)
                    is SysStatus -> handleSysStatus(message.payload as SysStatus)
                    is GlobalPositionInt -> handleGlobalPositionInt(message.payload as GlobalPositionInt)
                    is Attitude -> handleAttitude(message.payload as Attitude)
                    is VfrHud -> handleVfrHud(message.payload as VfrHud)
                    is BatteryStatus -> handleBatteryStatus(message.payload as BatteryStatus)
                    is GpsRawInt -> handleGpsRawInt(message.payload as GpsRawInt)
                    is HomePosition -> handleHomePosition(message.payload as HomePosition)
                    is RcChannels -> handleRcChannels(message.payload as RcChannels)
                    is ParamValue -> handleParamValue(message.payload as ParamValue)
                    // Add more message types as needed
                }
            }
        } catch (e: Exception) {
            // Log error but don't crash
            e.printStackTrace()
        }
    }
    
    private fun handleHeartbeat(heartbeat: Heartbeat, systemId: Int, componentId: Int) {
        val isArmed = (heartbeat.baseMode().value and MAV_MODE_FLAG_SAFETY_ARMED) != 0
        val flightMode = getFlightModeName(heartbeat.customMode(), heartbeat.autopilot())
        
        _vehicleState.value = _vehicleState.value.copy(
            isConnected = true,
            isArmed = isArmed,
            flightMode = flightMode,
            autopilotType = heartbeat.autopilot().toString(),
            systemId = systemId,
            componentId = componentId
        )
    }
    
    private fun handleSysStatus(sysStatus: SysStatus) {
        val batteryPct = sysStatus.batteryRemaining() / 100.0f
        val voltage = sysStatus.voltageBattery() / 1000.0f
        
        _vehicleState.value = _vehicleState.value.copy(
            batteryPercentage = batteryPct,
            batteryVoltage = voltage
        )
    }
    
    private fun handleGlobalPositionInt(globalPos: GlobalPositionInt) {
        _vehicleState.value = _vehicleState.value.copy(
            latitude = globalPos.lat() / 1e7,
            longitude = globalPos.lon() / 1e7,
            altitudeAMSL = globalPos.alt() / 1000.0f,
            altitudeRelative = globalPos.relativeAlt() / 1000.0f
        )
    }
    
    private fun handleAttitude(attitude: Attitude) {
        _vehicleState.value = _vehicleState.value.copy(
            roll = Math.toDegrees(attitude.roll().toDouble()).toFloat(),
            pitch = Math.toDegrees(attitude.pitch().toDouble()).toFloat(),
            yaw = Math.toDegrees(attitude.yaw().toDouble()).toFloat()
        )
    }
    
    private fun handleVfrHud(vfrHud: VfrHud) {
        _vehicleState.value = _vehicleState.value.copy(
            airSpeed = vfrHud.airspeed(),
            groundSpeed = vfrHud.groundspeed(),
            altitudeAMSL = vfrHud.alt()
        )
    }
    
    private fun handleBatteryStatus(batteryStatus: BatteryStatus) {
        // Additional battery info can be processed here
    }
    
    private fun handleGpsRawInt(gpsRaw: GpsRawInt) {
        _vehicleState.value = _vehicleState.value.copy(
            gpsFixType = gpsRaw.fixType().value,
            satelliteCount = gpsRaw.satellitesVisible()
        )
    }
    
    private fun handleHomePosition(homePosition: HomePosition) {
        _vehicleState.value = _vehicleState.value.copy(
            homeLatitude = homePosition.latitude() / 1e7,
            homeLongitude = homePosition.longitude() / 1e7
        )
    }
    
    private fun handleRcChannels(rcChannels: RcChannels) {
        // RC channel data can be processed here if needed
    }
    
    private fun handleParamValue(paramValue: ParamValue) {
        // Parameter values received
    }
    
    private fun getFlightModeName(customMode: Long, autopilot: MavAutopilot): String {
        // Simplified flight mode mapping - in real implementation, this would be more comprehensive
        return when (autopilot) {
            MavAutopilot.MAV_AUTOPILOT_ARDUPILOTMEGA -> {
                when (customMode.toInt()) {
                    0 -> "Stabilize"
                    1 -> "Acro"
                    2 -> "AltHold" 
                    3 -> "Auto"
                    4 -> "Guided"
                    5 -> "Loiter"
                    6 -> "RTL"
                    7 -> "Circle"
                    9 -> "Land"
                    else -> "Mode $customMode"
                }
            }
            MavAutopilot.MAV_AUTOPILOT_PX4 -> {
                when (customMode.toInt()) {
                    65536 -> "Manual"
                    65540 -> "Stabilized"
                    65544 -> "Altitude"
                    65548 -> "Position"
                    65552 -> "Auto"
                    65556 -> "Acro"
                    else -> "Mode $customMode"
                }
            }
            else -> "Mode $customMode"
        }
    }
    
    override suspend fun sendCommand(
        command: MavCmd, param1: Float, param2: Float, param3: Float, 
        param4: Float, param5: Float, param6: Float, param7: Float
    ): Result<Unit> {
        return try {
            val commandMessage = CommandLong.builder()
                .targetSystem(_vehicleState.value.systemId)
                .targetComponent(_vehicleState.value.componentId)
                .command(command)
                .confirmation(0)
                .param1(param1)
                .param2(param2)
                .param3(param3)
                .param4(param4)
                .param5(param5)
                .param6(param6)
                .param7(param7)
                .build()
            
            sendMessage(commandMessage)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun sendHeartbeat() {
        val heartbeat = Heartbeat.builder()
            .type(MavType.MAV_TYPE_GCS)
            .autopilot(MavAutopilot.MAV_AUTOPILOT_INVALID)
            .baseMode(0)
            .customMode(0)
            .systemStatus(MavState.MAV_STATE_ACTIVE)
            .mavlinkVersion(3)
            .build()
        
        sendMessage(heartbeat)
    }
    
    override suspend fun requestParameterList() {
        val paramRequest = ParamRequestList.builder()
            .targetSystem(_vehicleState.value.systemId)
            .targetComponent(_vehicleState.value.componentId)
            .build()
        
        sendMessage(paramRequest)
    }
    
    override suspend fun requestMissionList() {
        val missionRequest = MissionRequestList.builder()
            .targetSystem(_vehicleState.value.systemId)
            .targetComponent(_vehicleState.value.componentId)
            .missionType(MavMissionType.MAV_MISSION_TYPE_MISSION)
            .build()
        
        sendMessage(missionRequest)
    }
    
    override suspend fun setParameter(paramId: String, value: Float): Result<Unit> {
        return try {
            val paramSet = ParamSet.builder()
                .targetSystem(_vehicleState.value.systemId)
                .targetComponent(_vehicleState.value.componentId)
                .paramId(paramId)
                .paramValue(value)
                .paramType(MavParamType.MAV_PARAM_TYPE_REAL32)
                .build()
            
            sendMessage(paramSet)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun setMode(baseMode: Int, customMode: Long): Result<Unit> {
        return try {
            val setMode = SetMode.builder()
                .targetSystem(_vehicleState.value.systemId)
                .baseMode(MavModeFlag.fromValue(baseMode))
                .customMode(customMode)
                .build()
            
            sendMessage(setMode)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private suspend fun sendMessage(message: Any): Result<Unit> {
        return try {
            val mavlinkMessage = MavlinkMessage.builder()
                .systemId(gcsSystemId)
                .componentId(gcsComponentId)
                .payload(message)
                .build()
            
            val outputStream = ByteArrayOutputStream()
            messageWriter?.write(outputStream, mavlinkMessage)
            val data = outputStream.toByteArray()
            
            connectionManager.sendData(data)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    companion object {
        private const val MAV_MODE_FLAG_SAFETY_ARMED = 128
    }
}