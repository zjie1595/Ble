package com.zj.ble.model;

/**
 * Created by dhj on 2020/7/26
 * 全局常量指令
 */
public class CmdConstant {

    public final static byte OPEN_MACHINE = (byte) 0x81;
    public final static byte FINISH_PHOTO = (byte) 0x82;
    public final static byte SCAN_PROJECT = (byte) 0x83;
    public final static byte SCAN_CODE = (byte) 0x84;
    public final static byte CHECK_VERSION = (byte) 0x85;

    public final static byte CHECK_SPOT_NO = (byte) 0x71;
    public final static byte LOCATION_INFO = (byte) 0x72;

    public final static byte WELD_INFO = (byte) 0x91;
    public final static byte START_WORK = (byte) 0x92;
    public final static byte HISTORY_LIST = (byte) 0x93;
    public final static byte SPOT_INFO = (byte) 0x94;

    public final static byte TAKE_PHOTO = (byte) 0x11;
    public final static byte FINISH_INFO = (byte) 0x12;


    public final static byte OPEN_MACHINE_NEW = (byte) 0xA1;
    public final static byte MODIFY_MACHINE = (byte) 0xA2;
    public final static byte START_WORK_NEW = (byte) 0xA3;
    public final static byte CURRENT_CONFIGURATION = (byte) 0xA4;

}
