package com.zj.ble.model;

/**
 * Created by DELL on 2020/8/13
 */

public class OpenMachineInfoBean {
    private String welderType;
    private String welderNum;

    public String getWelderType() {
        return welderType;
    }

    public void setWelderType(String welderType) {
        this.welderType = welderType;
    }

    public String getWelderNum() {
        return welderNum;
    }

    public void setWelderNum(String welderNum) {
        this.welderNum = welderNum;
    }

    @Override
    public String toString() {
        return "OpenMachineInfoBean{" +
                "welderType='" + welderType + '\'' +
                ", welderNum='" + welderNum + '\'' +
                '}';
    }
}
