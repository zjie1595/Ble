package com.zj.sample_compose

import android.app.Application
import com.clj.fastble.BleManager

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        BleManager.getInstance()
            .init(this)
    }
}