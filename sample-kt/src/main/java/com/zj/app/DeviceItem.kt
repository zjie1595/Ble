package com.zj.app

import com.clj.fastble.data.BleDevice

data class DeviceItem(
    val device: BleDevice,
    val connected: Boolean = false
)
