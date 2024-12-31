package com.zj.ble.model;

/**
 * 检查焊机程序版本号以及地区
 *
 * @author zj
 * @date 2024/05/16
 */
public class CheckVersionBean {

    /**
     * 焊机型号
     */
    private String welderType;

    /**
     * 焊机铭牌编号
     */
    private String welderNum;

    /**
     * 版本地区
     */
    private String versionArea;

    /**
     * 版本名称
     */
    private String versionName;

    /**
     * 版本code号
     */
    private int versionCode;

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

    public String getVersionArea() {
        return versionArea;
    }

    public void setVersionArea(String versionArea) {
        this.versionArea = versionArea;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    @Override
    public String toString() {
        return "CheckVersionBean{" +
                "welderType='" + welderType + '\'' +
                ", welderNum='" + welderNum + '\'' +
                ", versionArea='" + versionArea + '\'' +
                ", versionName='" + versionName + '\'' +
                ", versionCode=" + versionCode +
                '}';
    }
}
