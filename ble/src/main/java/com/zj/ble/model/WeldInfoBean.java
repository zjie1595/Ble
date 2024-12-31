package com.zj.ble.model;

import androidx.annotation.NonNull;

/**
 * Created by DELL on 2020/7/16
 */

public class WeldInfoBean {
    private String welderNum;

    public String getWelderNum() {
        return welderNum;
    }

    public void setWelderNum(String welderNum) {
        this.welderNum = welderNum;
    }

    @Override
    public String toString() {
        return "WeldInfoBean{" +
                "welderNum='" + welderNum + '\'' +
                '}';
    }
}
