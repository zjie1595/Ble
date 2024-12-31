package com.zj.ble.codec;

import android.util.Log;

import java.nio.ByteBuffer;

/**
 * Created by DELL on 2020/7/18.
 */

public class EncodeUtil {

    private byte flow_num = 1; //流水号

    public static EncodeUtil INSTNCE;

    private EncodeUtil() {
    }

    public static EncodeUtil getINSTNCE() {
        if (INSTNCE == null) {
            synchronized (EncodeUtil.class) {
                INSTNCE = new EncodeUtil();
            }
        }
        return INSTNCE;
    }

    public ByteBuffer encode(byte[] data, byte cmd) {
        if (cmd == (byte) 0x94) {
            flow_num = 4;
        }

        int package_num = 0; //数据包个数  头包 + 数据包 n * 20 + 尾包
        int data_len = data.length;
        if (data_len % 20 == 0) {
            package_num = data_len / 20;
        } else {
            package_num = data_len / 20 + 1;
        }
        Log.d("buffer", "package_num" + package_num + "");
        int capacity = (package_num + 2) * 20;
        ByteBuffer buffer = ByteBuffer.allocate(capacity);
        //组装头包
        buffer.put((byte) 0xaa); //命令头
        buffer.put(cmd); //命令号
        buffer.put(flow_num); //流水号
        buffer.putShort(((short) package_num));
        buffer.putShort((short) data_len);
        buffer.putShort((short) 0); // 头包crc校验
        buffer.position(20);
        //组装业务数据
        buffer.put(data);
        //补 0 操作
        if (data_len % 20 != 0) {
            buffer.position((package_num + 1) * 20);
        }

        //组装尾包
        buffer.put((byte) 0xbb);
        buffer.put(cmd);
        buffer.put(flow_num);
        buffer.putShort((short) package_num);
        buffer.putShort((short) data_len);
        buffer.putShort((byte) 0); //crc
        buffer.putShort((byte) 0); //crc
        flow_num++;
        int count = 0;
        for (byte b : buffer.array()) {
            Log.d("buffer", count++ + " :" + b);
        }
        return buffer;
    }

}
