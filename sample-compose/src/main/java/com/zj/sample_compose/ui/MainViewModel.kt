package com.zj.sample_compose.ui

import android.Manifest
import android.bluetooth.BluetoothGatt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.blankj.utilcode.util.ToastUtils
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.zj.ble.BleUtils
import com.zj.ble.CmdParseExecute
import com.zj.ble.model.DecodeBean
import com.zj.ble.model.OpenMachineInfoBean
import com.zj.ble.model.PhotoFinishInfoBean
import com.zj.ble.model.SpotNoCheckBean
import com.zj.ble.model.WeldInfoBean
import com.zj.ble.model.WeldJointInfoBean
import com.zj.ble.model.WeldJointInfoDrBean
import com.zj.sample_compose.toJsonString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * 主 UI 状态
 * @author zj
 * @date 2024/12/31
 * @constructor 创建[MainUiState]
 * @param [messageList] 消息列表
 * @param [deviceList] 设备列表
 * @param [welderNum] 焊工编号
 * @param [operatorNum] 运算符 num
 * @param [requestCode] 请求代码
 * @param [resultCode] 结果代码
 * @param [projectNum] 项目编号
 * @param [weldNum] 焊缝编号
 * @param [weldNumCheckResult] 焊缝编号检查结果
 * @param [weldingPortNum] 焊接端口编号
 */
data class MainUiState(
    val messageList: List<String> = emptyList(),
    val deviceList: List<DeviceUiState> = emptyList(),
    val welderNum: String = "",
    val operatorNum: String = "MK992211",
    val requestCode: String = "1",
    val resultCode: String = "1",
    val projectNum: String = "1230215454",
    val weldNum: String = "D001",
    val weldNumCheckResult: String = "0",
    val weldingPortNum: String = ""
)

data class DeviceUiState(
    val connectStatus: Int = 0,     // 0:默认状态 1：连接中 2：已连接 3：连接断开 -1：连接失败
    val bleDevice: BleDevice
)

class MainViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.stateIn(
        viewModelScope, SharingStarted.WhileSubscribed(5000),
        MainUiState()
    )

    private val weldType get() = _uiState.value.welderNum.substring(0, 6)
    private val welderNum get() = _uiState.value.welderNum.substring(6)

    init {
        BleUtils.setNotifyListener(object : BleUtils.Callback {
            override fun takePhoto(decodeBean: DecodeBean?) {
                LogUtils.d("焊机请求拍照", decodeBean.toJsonString())
            }

            override fun weldingFinish(decodeBean: DecodeBean?) {
                LogUtils.d("焊机完成", decodeBean.toJsonString())
            }
        })
        LogUtils.getConfig()
            .setBorderSwitch(false)
            .setLogHeadSwitch(false)
            .setOnConsoleOutputListener { type, tag, content ->
                val list = _uiState.value.messageList.toMutableList()
                list += content
                _uiState.update {
                    it.copy(
                        messageList = list
                    )
                }
            }
        BleUtils.onWeldNumberVerification(object : CmdParseExecute.CheckSpotNoListener {
            override fun setSpotNoData(spotNoCheckBean: SpotNoCheckBean?) {
                LogUtils.d("焊口编号验证", spotNoCheckBean.toJsonString())
                _uiState.update {
                    it.copy(
                        weldNum = spotNoCheckBean!!.welderPortNumber,
                        weldingPortNum = spotNoCheckBean.welderPortNumber
                    )
                }
            }

            override fun errorData(decodeBean: DecodeBean?) {
                LogUtils.d("焊口编号验证失败", decodeBean)
            }
        })
    }

    fun onWeldingPortNumChange(weldingPortNum: String) {
        _uiState.update {
            it.copy(
                weldingPortNum = weldingPortNum
            )
        }
    }

    fun onWelderNumChange(welderNum: String) {
        _uiState.update {
            it.copy(
                welderNum = welderNum
            )
        }
    }

    fun onOperatorNumChange(operatorNum: String) {
        _uiState.update {
            it.copy(
                operatorNum = operatorNum
            )
        }
    }

    fun onRequestCodeChange(requestCode: String) {
        _uiState.update {
            it.copy(
                requestCode = requestCode
            )
        }
    }

    fun onResultCodeChange(resultCode: String) {
        _uiState.update {
            it.copy(
                resultCode = resultCode
            )
        }
    }

    fun onProjectNumChange(projectNum: String) {
        _uiState.update {
            it.copy(
                projectNum = projectNum
            )
        }
    }

    fun onWeldNumChange(weldNum: String) {
        _uiState.update {
            it.copy(
                weldNum = weldNum
            )
        }
    }

    fun onWeldNumCheckResultChange(weldNumCheckResult: String) {
        _uiState.update {
            it.copy(
                weldNumCheckResult = weldNumCheckResult
            )
        }
    }

    fun onClick(title: String) {
        when (title) {
            "扫描蓝牙设备" -> {
                scan()
            }

            "获取焊机信息" -> {
                BleUtils.getWelderInformation(object : CmdParseExecute.WeldInfoShowListener {
                    override fun setWeldDataShow(weldInfoBean: WeldInfoBean?) {
                        LogUtils.d("获取焊机信息成功", weldInfoBean.toJsonString())
                        _uiState.update {
                            it.copy(
                                welderNum = weldInfoBean!!.welderNum
                            )
                        }
                    }

                    override fun errorData(decodeBean: DecodeBean?) {
                        LogUtils.d("获取焊机信息失败", decodeBean.toJsonString())
                    }
                })
            }

            "授权人员信息" -> {
                BleUtils.checkOperatorInfo(
                    weldType,
                    welderNum,
                    _uiState.value.operatorNum,
                    object : CmdParseExecute.OpenMachineInfoListener {
                        override fun setOpenData(openMachineInfoBean: OpenMachineInfoBean?) {
                            LogUtils.d("授权人员信息成功", openMachineInfoBean.toJsonString())
                        }

                        override fun errorData(decodeBean: DecodeBean?) {
                            LogUtils.d("授权人员信息失败", decodeBean.toJsonString())
                        }
                    })
            }

            "拍照完成信息" -> {
                BleUtils.onPhotoCaptured(
                    weldType,
                    welderNum,
                    _uiState.value.requestCode.toInt(),
                    _uiState.value.resultCode.toInt(),
                    object : CmdParseExecute.PhotoFinishInfoListener {
                        override fun setPhotoData(photoFinishInfoBean: PhotoFinishInfoBean?) {
                            LogUtils.d("拍照完成信息发送成功", photoFinishInfoBean.toJsonString())
                        }

                        override fun errorPhotoData(decodeBean: DecodeBean?) {
                            LogUtils.d("拍照完成信息发送失败", decodeBean.toJsonString())
                        }
                    })
            }

            "授权工程信息" -> {
                BleUtils.checkProjectInfo(
                    weldType,
                    welderNum,
                    _uiState.value.projectNum,
                    object : CmdParseExecute.ScanProjectInfoListener {
                        override fun setProjectData(openMachineInfoBean: OpenMachineInfoBean?) {
                            LogUtils.d("授权工程信息成功", openMachineInfoBean.toJsonString())
                        }

                        override fun errorData(decodeBean: DecodeBean?) {
                            LogUtils.d("授权工程信息失败", decodeBean.toJsonString())
                        }
                    })
            }

            "焊口编号验证" -> {
                BleUtils.checkWeldJointNumber(
                    weldType,
                    welderNum,
                    _uiState.value.weldNum,
                    _uiState.value.weldNumCheckResult.toInt(),
                    object : CmdParseExecute.CheckSpotNoListener {
                        override fun setSpotNoData(spotNoCheckBean: SpotNoCheckBean?) {
                            LogUtils.d("焊口编号验证成功", spotNoCheckBean.toJsonString())
                        }

                        override fun errorData(decodeBean: DecodeBean?) {
                            LogUtils.d("焊口编号验证失败", decodeBean.toJsonString())
                        }
                    })
            }

            "获取焊口焊接信息" -> {
                BleUtils.getWeldJointInfo(
                    _uiState.value.projectNum,
                    _uiState.value.weldingPortNum,
                    _uiState.value.welderNum,
                    object : CmdParseExecute.WeldInfoListener {
                        override fun setWeldData(decodeBean: WeldJointInfoBean?) {
                            LogUtils.d("获取焊口焊接信息成功", decodeBean.toJsonString())
                        }

                        override fun setWeldDrData(decodeBean: WeldJointInfoDrBean?) {
                            LogUtils.d("获取焊口焊接信息成功", decodeBean.toJsonString())
                        }

                        override fun error(decodeBean: DecodeBean?) {
                            LogUtils.d("获取焊口焊接信息失败", decodeBean.toJsonString())
                        }

                        override fun notify(decodeBean: DecodeBean?) {
                            LogUtils.d("获取焊口焊接信息？？？", decodeBean.toJsonString())
                        }
                    })
            }
        }
    }

    private fun scan() {
        PermissionUtils.permission(
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        ).callback { isAllGranted, _, _, _ ->
            if (!isAllGranted) {
                ToastUtils.showLong("权限获取失败")
                return@callback
            }
            val deviceList = mutableListOf<BleDevice>()
            BleManager.getInstance()
                .scan(object : BleScanCallback() {
                    override fun onScanStarted(success: Boolean) {
                        LogUtils.d("扫描开始", success)
                    }

                    override fun onScanning(bleDevice: BleDevice?) {
                        if (bleDevice == null || bleDevice.name.isNullOrBlank()) {
                            return
                        }
                        if (deviceList.any { it.name == bleDevice.name }) {
                            return
                        }
                        if (deviceList.any { it.mac == bleDevice.mac }) {
                            return
                        }
                        deviceList += bleDevice
                        _uiState.update {
                            it.copy(
                                deviceList = deviceList.map { device -> DeviceUiState(bleDevice = device) }
                            )
                        }
                    }

                    override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                        LogUtils.d("扫描结束")
                    }
                })
        }.request()
    }

    fun connect(item: DeviceUiState) {
        PermissionUtils.permission(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        ).callback { isAllGranted, _, _, _ ->
            if (!isAllGranted) {
                ToastUtils.showLong("权限获取失败")
                return@callback
            }
            updateStatus(item.bleDevice, 1)
            BleManager.getInstance()
                .connect(item.bleDevice, object : BleGattCallback() {
                    override fun onStartConnect() {
                        LogUtils.d("开始连接")
                    }

                    override fun onConnectFail(bleDevice: BleDevice?, exception: BleException?) {
                        LogUtils.d("连接失败", bleDevice?.name, exception)
                        updateStatus(item.bleDevice, -1)
                    }

                    override fun onConnectSuccess(
                        bleDevice: BleDevice?,
                        gatt: BluetoothGatt?,
                        status: Int
                    ) {
                        LogUtils.d("连接成功", bleDevice?.name)
                        updateStatus(bleDevice, 2)
                        BleUtils.onBleConnected(2, gatt)
                        BleManager.getInstance()
                            .notify(
                                bleDevice,
                                BleUtils.getNotifyService().uuid.toString(),
                                BleUtils.getNotifyCharacteristic().uuid.toString(),
                                object : BleNotifyCallback() {
                                    override fun onNotifySuccess() {
                                        LogUtils.d("通知打开成功")
                                    }

                                    override fun onNotifyFailure(exception: BleException?) {
                                        LogUtils.d("通知打开失败", exception)
                                    }

                                    override fun onCharacteristicChanged(data: ByteArray?) {
                                        BleUtils.onCharacteristicChanged(data)
                                        LogUtils.d(
                                            "焊机发送的数据",
                                            ConvertUtils.bytes2HexString(data)
                                        )
                                    }
                                }
                            )
                    }

                    override fun onDisConnected(
                        isActiveDisConnected: Boolean,
                        device: BleDevice?,
                        gatt: BluetoothGatt?,
                        status: Int
                    ) {
                        LogUtils.d("连接断开", isActiveDisConnected, device?.name, status)
                        updateStatus(item.bleDevice, 3)
                    }
                })
        }.request()
    }

    private fun updateStatus(bleDevice: BleDevice?, status: Int) {
        val deviceList = _uiState.value.deviceList.toMutableList()
        val index = deviceList.indexOfFirst { it.bleDevice.mac == bleDevice?.mac }
        val old = deviceList.getOrNull(index)
        val new = old?.copy(
            connectStatus = status
        )
        deviceList[index] = new!!
        _uiState.update {
            it.copy(
                deviceList = deviceList
            )
        }
    }
}