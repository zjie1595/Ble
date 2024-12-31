package com.zj.ble.model;

/**
 * Created by DELL on 2020/7/16
 */

public class SpotWorkBean {
    private String projectNum;
    private String weldingPortNum;
    private String welderNum;
    private int respCode;

    public String getProjectNum() {
        return projectNum;
    }

    public void setProjectNum(String projectNum) {
        this.projectNum = projectNum;
    }

    public String getWeldingPortNum() {
        return weldingPortNum;
    }

    public void setWeldingPortNum(String weldingPortNum) {
        this.weldingPortNum = weldingPortNum;
    }

    public String getWelderNum() {
        return welderNum;
    }

    public void setWelderNum(String welderNum) {
        this.welderNum = welderNum;
    }

    public int getRespCode() {
        return respCode;
    }

    public void setRespCode(int respCode) {
        this.respCode = respCode;
    }
}
