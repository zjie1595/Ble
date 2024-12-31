package com.zj.sample_compose

import com.blankj.utilcode.util.GsonUtils

fun Any?.toJsonString(): String? {
    return GsonUtils.toJson(this)
}