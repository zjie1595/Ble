package com.zj.ble.model;

/**
 * Created by DELL on 2020/09/08
 * 定位信息的bean类
 */

public class LocationInfoBean {
    private String welderType;
    private String welderNum;
    private String longitude;
    private String latitude;

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

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }
}
