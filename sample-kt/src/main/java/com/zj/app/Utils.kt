package com.zj.app

import android.content.Context
import com.lxj.xpopup.XPopup
import com.lxj.xpopup.core.BasePopupView

fun Context.showLoading(title: String): BasePopupView {
    return XPopup.Builder(this)
        .asLoading(title)
        .show()
}