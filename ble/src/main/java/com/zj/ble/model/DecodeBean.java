package com.zj.ble.model;

import java.util.Arrays;

/**
 * Created by DELL on 2020/7/18
 */

public class DecodeBean {
      /**
     * 焊机回调类型
     */
    public int status = 0; //0 success   1 error   2 notify
    public byte cmd;
    public byte[] data;
    public short data_len;
    public byte errorCode;
    public short notifyCode;

    @Override
    public String toString() {
        return "DecodeBean{" +
                "status=" + status +
                ", cmd=" + cmd +
                ", data=" + Arrays.toString(data) +
                ", data_len=" + data_len +
                ", errorCode=" + errorCode +
                ", notifyCode=" + notifyCode +
                '}';
    }
}
