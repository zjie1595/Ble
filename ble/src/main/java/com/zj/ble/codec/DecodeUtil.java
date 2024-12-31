package com.zj.ble.codec;

import android.util.Log;

import com.zj.ble.CmdParseExecute;
import com.zj.ble.model.DecodeBean;

import java.nio.ByteBuffer;

/**
 * Created by DELL on 2020/7/18.
 */

public class DecodeUtil {

    public static DecodeUtil INSTNCE;

    CmdParseExecute cmdParseExecute = CmdParseExecute.getINSTNCE();

    private DecodeUtil() {
    }

    public static DecodeUtil getINSTNCE() {
        if (INSTNCE == null) {
            synchronized (DecodeUtil.class) {
                INSTNCE = new DecodeUtil();
            }
        }
        return INSTNCE;
    }

    public final byte HEAD = 0x01;
    public final byte CMD = 0x02;
    public final byte FLOW = 0x03;
    public final byte PACK_NUM = 0x04;
    public final byte DATA_LEN = 0x05;
    public final byte CRC_HEAD = 0x06;
    public final byte DATA = 0x07;
    public final byte END = 0x08;
    public final byte CODE = 0x09;
    public final byte CRC_CHECK = 0x10;
    byte type = 0;
    int point = 0;
    byte[] data_len_byte = new byte[2];
    byte[] data_byte;
    short data_len = 0; //数据长度

    int parsingMode = 0; // 0  normal   1 error  2 notify

    byte[] data_code = new byte[2];
    short code_len = 0;

    public DecodeBean decodeBean = new DecodeBean();

    public boolean isFirst = true;

    public void decode(byte data, int dataType) {
        Log.d("decode", 0 + "==start:  " + data + "，解析模式：" + parsingMode);

        if (isFirst) {
            if (data == (byte) 0xAA) {
                Log.d("decode", 0 + "==start:  只解析一次头包==" + isFirst);

                type = HEAD;
                parsingMode = 0;

                isFirst = false;
            }
        } else {
            Log.d("decode", 0 + "==start:  不再解析头包==" + isFirst + "，类型==" + type);
        }

        if (isFirst) {
            if (data == (byte) 0xEE) {
                Log.d("decode", 0 + "==start:  只解析一次错误==" + isFirst);

                type = HEAD;
                parsingMode = 1;

                isFirst = false;
            }
        } else {
            Log.d("decode", 0 + "==start:  不再解析错误==" + isFirst + "，类型==" + type);
        }

        if (isFirst) {
            if (data == (byte) 0xDD) {
                Log.d("decode", 0 + "==start:  只解析一次通知==" + isFirst + "，类型==" + type);

                type = HEAD;
                parsingMode = 2;

                isFirst = false;
            }
        } else {
            Log.d("decode", 0 + "==start:  不再解析通知==" + isFirst);
        }

        if (parsingMode == 0) {
            decodeNormal(data, type, dataType);
        } else if (parsingMode == 1) {
            decodeError(data, type, dataType);
        } else {
            decodeNotify(data, type, dataType);
        }
    }


    public void decodeNormal(byte data, int type1, int dataType) {
        switch (type1) {
            case HEAD:
                Log.d("decode", 0 + "");
                point = 0;
                decodeBean.status = 0;
                type = CMD;
                break;
            case CMD:
                Log.d("decode", 1 + "");
                //拿到命令号
                decodeBean.cmd = data;
                type = FLOW;
                break;
            case FLOW:
                Log.d("decode", 2 + "");
                //流水
                point = 3;
                type = PACK_NUM;
                break;
            case PACK_NUM:
                Log.d("decode", 3 + "");
                //包个数
                point++;
                if (point == 5) {
                    type = DATA_LEN;
                }
                break;
            case DATA_LEN:
                Log.d("decode", 4 + "");
                //数据长度
                if (point == 5) {
                    data_len_byte[0] = data;
                }
                if (point == 6) {
                    data_len_byte[1] = data;
                    data_len = ByteBuffer.wrap(data_len_byte).getShort();
                    Log.d("decode_len", "decode_len :" + data_len);
                    decodeBean.data_len = data_len;
                    type = CRC_HEAD;
                }
                point++;
                break;
            case CRC_HEAD:
                Log.d("decode", 5 + "");
                point++;
                if (point == 20) {
                    type = DATA;
                }
                break;
            case DATA:
                Log.d("decode", 6 + "，数据长度为" + data_len);

                if (data_len <= 0) {
                    Log.d("decode", 6 + "数据长度为0");
                    type = END;
                    return;
                }

                if (point == 20) {
                    data_byte = new byte[data_len];
                }

                data_byte[point - 20] = data;
                point++;
                if (point - 20 == data_len) {
                    Log.d("decode1", "point为" + (point - 20));

                    Log.d("decode1", 6 + "，数据长度为" + data_len);

                    decodeBean.data = data_byte;
                    Log.d("decode_str", new String(data_byte, 0, data_byte.length - 1) + (byte) data);
                    // 数据回调给主页面
                    cmdParseExecute.parse(decodeBean, dataType);
                    type = END;
                }
                break;

            case END:
                Log.d("decode", 7 + "");

                isFirst = true;
                break;
            default:
                break;
        }
    }

    public void decodeError(byte data, int type1, int dataType) {
        switch (type1) {
            case HEAD:
                Log.d("decode-error", 0 + "");
                point = 0;
                decodeBean.status = 1;
                type = CMD;
                break;
            case CMD:
                Log.d("decode-error", 1 + "");
                //拿到命令号
                decodeBean.cmd = data;
                type = FLOW;
                break;
            case FLOW:
                Log.d("decode-error", 2 + "");
                //流水
                point = 3;
                type = CODE;
                break;
            case CODE:
                Log.d("decode-error", 3 + "");
                //错误码
                point = 4;
                decodeBean.errorCode = data;
                type = CRC_CHECK;
                break;
            case CRC_CHECK:
                point++;
                Log.d("decode-error", 4 + "," + point);

                if (point == 20) {
                    decodeBean.data_len = 0;
                    decodeBean.data = new byte[0];
                    cmdParseExecute.parse(decodeBean, dataType);
                    type = END;

                    isFirst = true;
                }
                break;
            case END:
                Log.d("decode-error", 5 + "");
                break;
        }
    }

    public void decodeNotify(byte data, int type1, int dataType) {
        switch (type1) {
            case HEAD:
                Log.d("decode-error", 0 + "");
                point = 0;
                decodeBean.status = 2;
                type = CMD;
                break;
            case CMD:
                Log.d("decode-error", 1 + "");
                //拿到命令号
                decodeBean.cmd = data;
                type = FLOW;
                break;
            case FLOW:
                Log.d("decode-error", 2 + "");
                //流水
                point++;
                if (point == 3) {
                    type = CODE;
                }
                break;
            case CODE:
                Log.d("decode-error", 3 + "");
                //错误码
                if (point == 4) {
                    data_code[0] = data;
                }
                if (point == 5) {
                    data_code[1] = data;
                    code_len = ByteBuffer.wrap(data_code).get();
                    decodeBean.notifyCode = code_len;
                    Log.d("decode-error", "code_len===" + code_len);
                    type = CRC_CHECK;
                }
                point++;
                break;
            case CRC_CHECK:
                Log.d("decode-error", 4 + "");
                point++;
                if (point == 7) {
                    decodeBean.data_len = 0;
                    decodeBean.data = new byte[0];
                    cmdParseExecute.parse(decodeBean, dataType);
                    type = END;
                }
                break;
            case END:
                Log.d("decode-error", 5 + "");
                isFirst = true;
                break;
        }
    }

}
