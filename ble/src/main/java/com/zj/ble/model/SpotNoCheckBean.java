package com.zj.ble.model;

/**
 * Created by DELL on 2020/09/08
 */

public class SpotNoCheckBean {
    private String welderType;//焊机型号
    private String welderNum;//焊机铭牌编号
    private String welderPortNumber;//业务焊口编号

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

    public String getWelderPortNumber() {
        return welderPortNumber;
    }

    public void setWelderPortNumber(String welderPortNumber) {
        this.welderPortNumber = welderPortNumber;
    }

    @Override
    public String toString() {
        return "SpotNoCheckBean{" +
                "welderType='" + welderType + '\'' +
                ", welderNum='" + welderNum + '\'' +
                ", welderPortNumber='" + welderPortNumber + '\'' +
                '}';
    }
}
