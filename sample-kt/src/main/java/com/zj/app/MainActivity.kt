package com.zj.app

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.ConvertUtils
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.PermissionUtils
import com.clj.fastble.BleManager
import com.clj.fastble.callback.BleGattCallback
import com.clj.fastble.callback.BleNotifyCallback
import com.clj.fastble.callback.BleScanCallback
import com.clj.fastble.data.BleDevice
import com.clj.fastble.exception.BleException
import com.drake.brv.utils.divider
import com.drake.brv.utils.models
import com.drake.brv.utils.setup
import com.zj.app.databinding.ActivityMainBinding
import com.zj.ble.BleUtils
import com.zj.ble.CmdParseExecute
import com.zj.ble.model.DecodeBean
import com.zj.ble.model.OpenMachineInfoBean
import com.zj.ble.model.PhotoFinishInfoBean
import com.zj.ble.model.SpotNoCheckBean
import com.zj.ble.model.WeldInfoBean
import com.zj.ble.model.WeldJointInfoBean
import com.zj.ble.model.WeldJointInfoDrBean


class MainActivity : AppCompatActivity() {

    private val deviceList = mutableListOf<DeviceItem>()

    private lateinit var binding: ActivityMainBinding

    private var weldInfoBean: WeldInfoBean? = null
    private val welderType get() = weldInfoBean?.welderNum?.substring(0, 6)
    private val welderNum get() = weldInfoBean?.welderNum?.substring(6)
    private val operatorNum get() = binding.etOperatorNumber.text?.toString()
    private val projectNum get() = binding.etProjectNumber.text?.toString()
    private val weldJointNum get() = binding.etWeldJointNum.text?.toString()
    private val portNum get() = binding.etPortNum.text?.toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        PermissionUtils.permission(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ).callback { _, _, _, _ ->

        }.request()

        binding.rv.divider(R.drawable.divider_horizontal).setup {
            addType<DeviceItem>(R.layout.item_device)
            onClick(R.id.item_device) {
                val popupView = showLoading("连接中...")
                val model = getModel<DeviceItem>()
                BleManager.getInstance()
                    .connect(model.device, object : BleGattCallback() {
                        override fun onStartConnect() {
                            LogUtils.d("开始连接")
                        }

                        override fun onConnectFail(
                            bleDevice: BleDevice?,
                            exception: BleException?
                        ) {
                            LogUtils.e("连接失败", bleDevice, exception)
                            popupView.dismiss()
                        }

                        override fun onConnectSuccess(
                            bleDevice: BleDevice?,
                            gatt: BluetoothGatt?,
                            status: Int
                        ) {
                            LogUtils.d("连接成功", bleDevice, status)
                            popupView.dismiss()
                            BleUtils.onBleConnected(2, gatt)
                            updateConnectStatus(bleDevice, true)
                            BleManager.getInstance().notify(bleDevice,
                                BleUtils.getNotifyService().uuid.toString(),
                                BleUtils.getNotifyCharacteristic().uuid.toString(),
                                object : BleNotifyCallback() {
                                    override fun onNotifySuccess() {
                                        LogUtils.d("通知订阅成功")
                                    }

                                    override fun onNotifyFailure(exception: BleException?) {
                                        LogUtils.e("通知订阅失败", exception)
                                    }

                                    override fun onCharacteristicChanged(data: ByteArray?) {
                                        LogUtils.d(
                                            "接收到焊机发送的数据",
                                            ConvertUtils.bytes2HexString(data),
                                            ConvertUtils.bytes2String(data)
                                        )
                                        BleUtils.onCharacteristicChanged(data)
                                    }
                                })
                        }

                        override fun onDisConnected(
                            isActiveDisConnected: Boolean,
                            device: BleDevice?,
                            gatt: BluetoothGatt?,
                            status: Int
                        ) {
                            LogUtils.d("连接断开", isActiveDisConnected, device, status)
                            updateConnectStatus(device, false)
                        }
                    })
            }
        }

        binding.rvLog.setup {
            addType<String>(R.layout.item_log)
        }

        LogUtils.getConfig()
            .setLogHeadSwitch(false)
            .setBorderSwitch(false)
            .setOnConsoleOutputListener { _, _, content ->
                val list = binding.rvLog.models?.toMutableList() ?: mutableListOf()
                list += content
                binding.rvLog.models = list
            }

        BleUtils.setNotifyListener(object : BleUtils.Callback {
            override fun takePhoto(decodeBean: DecodeBean?) {
                LogUtils.d("焊机通知拍照", decodeBean)
                BleUtils.onPhotoCaptured(
                    welderType,
                    welderNum,
                    decodeBean?.notifyCode?.toInt() ?: 0,
                    1,
                    object : CmdParseExecute.PhotoFinishInfoListener {
                        override fun setPhotoData(photoFinishInfoBean: PhotoFinishInfoBean?) {
                            LogUtils.d("拍照完成信息发送成功", photoFinishInfoBean)
                        }

                        override fun errorPhotoData(decodeBean: DecodeBean?) {
                            LogUtils.e("拍照完成信息发送失败", decodeBean)
                        }
                    })
            }

            override fun weldingFinish(decodeBean: DecodeBean?) {
                LogUtils.d("焊机通知焊接完成", decodeBean)
            }
        })
    }

    private fun updateConnectStatus(device: BleDevice?, connected: Boolean) {
        val index =
            deviceList.indexOfFirst { it.device.device.address == device?.device?.address }
        val newList = deviceList.toMutableList()
        newList[index] = DeviceItem(device!!, connected)
        binding.rv.models = newList
    }

    fun scan(view: View) {
        val a = object : BluetoothGattCallback() {

        }
        BleManager.getInstance()
            .scan(object : BleScanCallback() {
                override fun onScanStarted(success: Boolean) {
                    LogUtils.d("开始扫描", success)
                }

                override fun onScanning(bleDevice: BleDevice?) {
                    LogUtils.d("扫描中", bleDevice?.name)
                    if (bleDevice == null) {
                        return
                    }
                    if (bleDevice.name.isNullOrBlank()) {
                        return
                    }
                    if (deviceList.any { it.device.name == bleDevice.name }) {
                        return
                    }
                    if (deviceList.any { it.device.device.address == bleDevice.device.address }) {
                        return
                    }
                    deviceList += DeviceItem(
                        bleDevice, false
                    )
                    binding.rv.models = deviceList
                }

                override fun onScanFinished(scanResultList: MutableList<BleDevice>?) {
                    LogUtils.d("扫描结束")
                }
            })
    }

    fun getWelderInfo(view: View) {
        BleUtils.getWelderInformation(object : CmdParseExecute.WeldInfoShowListener {
            override fun setWeldDataShow(weldInfoBean: WeldInfoBean?) {
                LogUtils.d("获取焊机信息成功", weldInfoBean)
                this@MainActivity.weldInfoBean = weldInfoBean
            }

            override fun errorData(decodeBean: DecodeBean?) {
                LogUtils.e("获取焊机信息失败", decodeBean)
            }
        })
    }

    fun authOperatorInfo(view: View) {
        BleUtils.checkOperatorInfo(
            welderType,
            welderNum,
            operatorNum,
            object : CmdParseExecute.OpenMachineInfoListener {
                override fun setOpenData(openMachineInfoBean: OpenMachineInfoBean?) {
                    LogUtils.d("授权人员信息成功", openMachineInfoBean)
                }

                override fun errorData(decodeBean: DecodeBean?) {
                    LogUtils.e("授权人员信息失败", decodeBean)
                }
            })
    }

    fun onPhotoCaptured(view: View) {
        BleUtils.onPhotoCaptured(
            welderType,
            welderNum,
            1,
            1,
            object : CmdParseExecute.PhotoFinishInfoListener {
                override fun setPhotoData(photoFinishInfoBean: PhotoFinishInfoBean?) {
                    LogUtils.d("拍照完成信息发送成功", photoFinishInfoBean)
                }

                override fun errorPhotoData(decodeBean: DecodeBean?) {
                    LogUtils.e("拍照完成信息发送失败", decodeBean)
                }
            })
    }

    fun authProjectInfo(view: View) {
        BleUtils.checkProjectInfo(
            welderType,
            welderNum,
            projectNum,
            object : CmdParseExecute.ScanProjectInfoListener {
                override fun setProjectData(openMachineInfoBean: OpenMachineInfoBean?) {
                    LogUtils.d("授权工程信息成功", openMachineInfoBean)
                }

                override fun errorData(decodeBean: DecodeBean?) {
                    LogUtils.e("授权工程信息失败", decodeBean)
                }
            })
    }

    fun checkWeldJointNumber(view: View) {
        BleUtils.checkWeldJointNumber(
            welderType,
            welderNum,
            weldJointNum,
            0,
            object : CmdParseExecute.CheckSpotNoListener {
                override fun setSpotNoData(spotNoCheckBean: SpotNoCheckBean?) {
                    LogUtils.d("焊口编号验证成功", spotNoCheckBean)
                }

                override fun errorData(decodeBean: DecodeBean?) {
                    LogUtils.d("焊口编号验证失败", decodeBean)
                }
            })
    }

    fun getWeldJointInfo(view: View) {
        BleUtils.getWeldJointInfo(
            projectNum,
            System.currentTimeMillis().toString(),
            portNum,
            object : CmdParseExecute.WeldInfoListener {
                override fun setWeldData(decodeBean: WeldJointInfoBean?) {
                    LogUtils.d("获取焊口焊接信息成功", decodeBean)
                }

                override fun setWeldDrData(decodeBean: WeldJointInfoDrBean?) {
                    LogUtils.d("获取焊口焊接信息成功", decodeBean)
                }

                override fun error(decodeBean: DecodeBean?) {
                    LogUtils.d("获取焊口焊接信息失败", decodeBean)
                }

                override fun notify(decodeBean: DecodeBean?) {
                    LogUtils.d("获取焊口焊接信息成功", decodeBean)
                }
            })
    }
}