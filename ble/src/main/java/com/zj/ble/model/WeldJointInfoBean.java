package com.zj.ble.model;

/**
 * Created by DELL on 2020/7/18
 * 返回的焊口焊接信息类(热熔数据)
 */

public class WeldJointInfoBean {
    private String projectNum;
    private String weldingPortNum;
    private int weldingSerialNum;
    private String welderNum;
    private int pe;
    private int pipeDiameter;
    private int sdr;
    private String startTime;
    private int ambientTemp;
    private int pressureDrag;
    private int hotTemp;
    private int crimpingSettingValue;
    private int crimpingSettingTimeValue;
    private int crimpingActualValue;
    private int crimpingActualTimeValue;
    private int endothermicSettingValue;
    private int endothermicSettingTimeValue;
    private int endothermicActualValue;
    private int endothermicActualTimeValue;
    private int dockingRiseTime;
    private int coolDownSettingValue;
    private int coolDownSettingTimeValue;
    private int coolDownActualValue;
    private int coolDownActualTimeValue;
    private String endTime;
    private String  weldingStandards;
    private String  gps;
    private int weldingState;
    private int crimpingHeight;//卷边高度*10（mm）
    private int dataType;
    private String welder;//焊工编号
    private String managerNo;//项目经理编号
    private String corpId;//施工单位编号
    private String factoryNo;//焊机出厂编号
    private int activeFlag;//1合格  0不合格
    private String weldPic;//焊接图片，多张图片以逗号分隔

    public String getWeldPic() {
        return weldPic;
    }

    public void setWeldPic(String weldPic) {
        this.weldPic = weldPic;
    }

    public int getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(int activeFlag) {
        this.activeFlag = activeFlag;
    }

    public String getWelder() {
        return welder;
    }

    public void setWelder(String welder) {
        this.welder = welder;
    }

    public String getManagerNo() {
        return managerNo;
    }

    public void setManagerNo(String managerNo) {
        this.managerNo = managerNo;
    }

    public String getCorpId() {
        return corpId;
    }

    public void setCorpId(String corpId) {
        this.corpId = corpId;
    }

    public String getFactoryNo() {
        return factoryNo;
    }

    public void setFactoryNo(String factoryNo) {
        this.factoryNo = factoryNo;
    }

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
    }

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

    public int getWeldingSerialNum() {
        return weldingSerialNum;
    }

    public void setWeldingSerialNum(int weldingSerialNum) {
        this.weldingSerialNum = weldingSerialNum;
    }

    public String getWelderNum() {
        return welderNum;
    }

    public void setWelderNum(String welderNum) {
        this.welderNum = welderNum;
    }

    public int getPe() {
        return pe;
    }

    public void setPe(int pe) {
        this.pe = pe;
    }

    public int getPipeDiameter() {
        return pipeDiameter;
    }

    public void setPipeDiameter(int pipeDiameter) {
        this.pipeDiameter = pipeDiameter;
    }

    public int getSdr() {
        return sdr;
    }

    public void setSdr(int sdr) {
        this.sdr = sdr;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public int getAmbientTemp() {
        return ambientTemp;
    }

    public void setAmbientTemp(int ambientTemp) {
        this.ambientTemp = ambientTemp;
    }

    public int getPressureDrag() {
        return pressureDrag;
    }

    public void setPressureDrag(int pressureDrag) {
        this.pressureDrag = pressureDrag;
    }

    public int getHotTemp() {
        return hotTemp;
    }

    public void setHotTemp(int hotTemp) {
        this.hotTemp = hotTemp;
    }

    public int getCrimpingSettingValue() {
        return crimpingSettingValue;
    }

    public void setCrimpingSettingValue(int crimpingSettingValue) {
        this.crimpingSettingValue = crimpingSettingValue;
    }

    public int getCrimpingSettingTimeValue() {
        return crimpingSettingTimeValue;
    }

    public void setCrimpingSettingTimeValue(int crimpingSettingTimeValue) {
        this.crimpingSettingTimeValue = crimpingSettingTimeValue;
    }

    public int getCrimpingActualValue() {
        return crimpingActualValue;
    }

    public void setCrimpingActualValue(int crimpingActualValue) {
        this.crimpingActualValue = crimpingActualValue;
    }

    public int getCrimpingActualTimeValue() {
        return crimpingActualTimeValue;
    }

    public void setCrimpingActualTimeValue(int crimpingActualTimeValue) {
        this.crimpingActualTimeValue = crimpingActualTimeValue;
    }

    public int getEndothermicSettingValue() {
        return endothermicSettingValue;
    }

    public void setEndothermicSettingValue(int endothermicSettingValue) {
        this.endothermicSettingValue = endothermicSettingValue;
    }

    public int getEndothermicSettingTimeValue() {
        return endothermicSettingTimeValue;
    }

    public void setEndothermicSettingTimeValue(int endothermicSettingTimeValue) {
        this.endothermicSettingTimeValue = endothermicSettingTimeValue;
    }

    public int getEndothermicActualValue() {
        return endothermicActualValue;
    }

    public void setEndothermicActualValue(int endothermicActualValue) {
        this.endothermicActualValue = endothermicActualValue;
    }

    public int getEndothermicActualTimeValue() {
        return endothermicActualTimeValue;
    }

    public void setEndothermicActualTimeValue(int endothermicActualTimeValue) {
        this.endothermicActualTimeValue = endothermicActualTimeValue;
    }

    public int getDockingRiseTime() {
        return dockingRiseTime;
    }

    public void setDockingRiseTime(int dockingRiseTime) {
        this.dockingRiseTime = dockingRiseTime;
    }

    public int getCoolDownSettingValue() {
        return coolDownSettingValue;
    }

    public void setCoolDownSettingValue(int coolDownSettingValue) {
        this.coolDownSettingValue = coolDownSettingValue;
    }

    public int getCoolDownSettingTimeValue() {
        return coolDownSettingTimeValue;
    }

    public void setCoolDownSettingTimeValue(int coolDownSettingTimeValue) {
        this.coolDownSettingTimeValue = coolDownSettingTimeValue;
    }

    public int getCoolDownActualValue() {
        return coolDownActualValue;
    }

    public void setCoolDownActualValue(int coolDownActualValue) {
        this.coolDownActualValue = coolDownActualValue;
    }

    public int getCoolDownActualTimeValue() {
        return coolDownActualTimeValue;
    }

    public void setCoolDownActualTimeValue(int coolDownActualTimeValue) {
        this.coolDownActualTimeValue = coolDownActualTimeValue;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getWeldingStandards() {
        return weldingStandards;
    }

    public void setWeldingStandards(String weldingStandards) {
        this.weldingStandards = weldingStandards;
    }

    public String getGps() {
        return gps;
    }

    public void setGps(String gps) {
        this.gps = gps;
    }

    public int getWeldingState() {
        return weldingState;
    }

    public void setWeldingState(int weldingState) {
        this.weldingState = weldingState;
    }

    public int getCrimpingHeight() {
        return crimpingHeight;
    }

    public void setCrimpingHeight(int crimpingHeight) {
        this.crimpingHeight = crimpingHeight;
    }
}
