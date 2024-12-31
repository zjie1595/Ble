package com.zj.ble.model;

/**
 * Created by DELL on 2020/7/18
 * 返回的焊口焊接信息类(电熔数据)
 */

public class WeldJointInfoDrBean {
    private String projectNum;//项目编号
    private String weldingPortNum;//业务焊口号（焊口编号）
    private int weldingSerialNum;//焊口序列号
    private String welderNum;//焊机编号
    private int fittingType;//管件类型
    private int pipeDiameter;//管材直径
    private int sdr;//SDR
    private String startTime;//开始时间
    private int ambientTemp;//环境温度
    private int nominalResistance;//额定电阻
    private int measuredResistance;//实测电阻
    private int weldingVoltage;//焊接电压
    private int weldingActualVoltage;//焊接实际电压
    private int heat;//热量
    private int heatingAdjustmentTimeValue;//加热调整时间
    private int heatingSettingTimeValue;//加热设定时间
    private int coolDownSettingTimeValue;//冷却时间设定
    private int heatingActualTimeValue;//加热实际时间
    private int coolDownActualTimeValue;//冷却实际时间
    private String endTime;//结束时间
    private String  weldingStandards;//焊接标准
    private String  gps;//GPS定位信息
    private int weldingState;//焊接状态
    private int dataType;//焊机类型
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

    public int getFittingType() {
        return fittingType;
    }

    public void setFittingType(int fittingType) {
        this.fittingType = fittingType;
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

    public int getNominalResistance() {
        return nominalResistance;
    }

    public void setNominalResistance(int nominalResistance) {
        this.nominalResistance = nominalResistance;
    }

    public int getMeasuredResistance() {
        return measuredResistance;
    }

    public void setMeasuredResistance(int measuredResistance) {
        this.measuredResistance = measuredResistance;
    }

    public int getWeldingVoltage() {
        return weldingVoltage;
    }

    public void setWeldingVoltage(int weldingVoltage) {
        this.weldingVoltage = weldingVoltage;
    }

    public int getWeldingActualVoltage() {
        return weldingActualVoltage;
    }

    public void setWeldingActualVoltage(int weldingActualVoltage) {
        this.weldingActualVoltage = weldingActualVoltage;
    }

    public int getHeat() {
        return heat;
    }

    public void setHeat(int heat) {
        this.heat = heat;
    }

    public int getHeatingAdjustmentTimeValue() {
        return heatingAdjustmentTimeValue;
    }

    public void setHeatingAdjustmentTimeValue(int heatingAdjustmentTimeValue) {
        this.heatingAdjustmentTimeValue = heatingAdjustmentTimeValue;
    }

    public int getHeatingSettingTimeValue() {
        return heatingSettingTimeValue;
    }

    public void setHeatingSettingTimeValue(int heatingSettingTimeValue) {
        this.heatingSettingTimeValue = heatingSettingTimeValue;
    }

    public int getCoolDownSettingTimeValue() {
        return coolDownSettingTimeValue;
    }

    public void setCoolDownSettingTimeValue(int coolDownSettingTimeValue) {
        this.coolDownSettingTimeValue = coolDownSettingTimeValue;
    }

    public int getHeatingActualTimeValue() {
        return heatingActualTimeValue;
    }

    public void setHeatingActualTimeValue(int heatingActualTimeValue) {
        this.heatingActualTimeValue = heatingActualTimeValue;
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

    public int getDataType() {
        return dataType;
    }

    public void setDataType(int dataType) {
        this.dataType = dataType;
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

    public int getActiveFlag() {
        return activeFlag;
    }

    public void setActiveFlag(int activeFlag) {
        this.activeFlag = activeFlag;
    }
}
