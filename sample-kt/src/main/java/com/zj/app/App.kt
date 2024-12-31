package com.zj.app

import android.app.Application
import com.clj.fastble.BleManager
import com.drake.brv.utils.BRV

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        BleManager.getInstance()
            .init(this)

        BRV.modelId = BR.m
    }
}