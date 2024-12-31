package com.zj.ble;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.text.TextUtils;

import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.blankj.utilcode.util.ToastUtils;
import com.zj.ble.codec.DecodeUtil;
import com.zj.ble.codec.EncodeUtil;
import com.zj.ble.model.DecodeBean;

import java.nio.ByteBuffer;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

public class BleUtils {

    private static final ConcurrentLinkedDeque<byte[]> concurrentLinkedDeque = new ConcurrentLinkedDeque<>();
    private static BluetoothGatt bluetoothGatt;
    private static BluetoothGattCharacteristic writeCharacteristic;
    private static BluetoothGattCharacteristic notifyCharacteristic;
    private static BluetoothGattService writeService;
    private static BluetoothGattService notifyService;

    public static BluetoothGatt getBluetoothGatt() {
        return bluetoothGatt;
    }

    public static BluetoothGattCharacteristic getWriteCharacteristic() {
        return writeCharacteristic;
    }

    public static BluetoothGattCharacteristic getNotifyCharacteristic() {
        return notifyCharacteristic;
    }

    public static BluetoothGattService getWriteService() {
        return writeService;
    }

    public static BluetoothGattService getNotifyService() {
        return notifyService;
    }

    /**
     * 接收到焊机发送的数据
     *
     * @param data 数据
     */
    public static void onCharacteristicChanged(byte[] data) {
        concurrentLinkedDeque.add(data);
    }

    /**
     * 蓝牙连接成功
     *
     * @param dataType 焊机类型，1，热熔焊机，2，电熔焊机
     * @param gatt     gatt
     */
    public static void onBleConnected(int dataType, BluetoothGatt gatt) {
        new Thread(() -> {
            while (true) {
                byte[] data = concurrentLinkedDeque.poll();
                if (data != null && data.length > 0) {
                    for (byte datum : data) {
                        DecodeUtil.getINSTNCE().decode(datum, dataType);
                    }
                } else {
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
        bluetoothGatt = gatt;
        initServiceAndChara();
        gatt.setCharacteristicNotification(notifyCharacteristic, true);
    }

    public interface Callback {
        /**
         * 拍照请求
         *
         * @param decodeBean 解码 Bean
         */
        void takePhoto(DecodeBean decodeBean);

        /**
         * 焊接完成
         *
         * @param decodeBean 解码 Bean
         */
        void weldingFinish(DecodeBean decodeBean);
    }

    /**
     * 设置通知回调，在这里处理通知，如拍照请求、焊接完成
     *
     * @param callback 回调
     */
    public static void setNotifyListener(Callback callback) {
        CmdParseExecute.getINSTNCE().setNotifyListener(decodeBean -> {
            byte mCmd = decodeBean.cmd;
            if (mCmd == (byte) 0x11) {
                callback.takePhoto(decodeBean);
            } else {
                callback.weldingFinish(decodeBean);
            }
        });
    }

    /**
     * 初始化蓝牙的相关服务和特征
     */
    private static void initServiceAndChara() {
        List<BluetoothGattService> bluetoothGattServices = bluetoothGatt.getServices();
        for (BluetoothGattService bluetoothGattService : bluetoothGattServices) {
            List<BluetoothGattCharacteristic> characteristics = bluetoothGattService.getCharacteristics();
            for (BluetoothGattCharacteristic characteristic : characteristics) {
                int charaProp = characteristic.getProperties();
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE) > 0) {
                    writeCharacteristic = characteristic;
                    writeService = bluetoothGattService;
                }
                if ((charaProp & BluetoothGattCharacteristic.PROPERTY_NOTIFY) > 0) {
                    notifyCharacteristic = characteristic;
                    notifyService = bluetoothGattService;
                }
            }
        }
    }

    /**
     * 获取焊机信息
     *
     * @param listener 监听
     */
    public static void getWelderInformation(CmdParseExecute.WeldInfoShowListener listener) {
        CmdParseExecute.getINSTNCE().setDataShowListener(listener);
        byte[] data = new byte[0];
        ByteBuffer buffer = EncodeUtil.getINSTNCE().encode(data, (byte) 0x91);

        int send_num = buffer.capacity() / 20;
        byte[] data_send = new byte[20];
        buffer.position(0);

        for (int i = 0; i < send_num; i++) {
            buffer.get(data_send);
            write(data_send);
        }
    }

    /**
     * 授权人员信息
     *
     * @param welderType  焊机型号，样例：HWA250
     * @param welderNum   焊机铭牌编号，样例：170301009
     * @param operatorNum 焊机操作员编号，样例：MK992211
     * @param listener    监听
     */
    public static void checkOperatorInfo(
            String welderType,
            String welderNum,
            String operatorNum,
            CmdParseExecute.OpenMachineInfoListener listener
    ) {
        CmdParseExecute.getINSTNCE().setOpenDataListener(listener);
        ByteBuffer buffer1 = ByteBuffer.allocate(38);
        buffer1.put(welderType.getBytes());
        buffer1.put(welderNum.getBytes());
        for (int i = 0; i < (14 - welderNum.length()); i++) {
            buffer1.put((byte) 0x20);
        }
        buffer1.put(operatorNum.getBytes());
        for (int i = 0; i < (18 - operatorNum.getBytes().length); i++) {
            buffer1.put((byte) 0x20);
        }
        byte[] data = new byte[buffer1.capacity()];
        buffer1.position(0);
        buffer1.get(data);
        ByteBuffer buffer = EncodeUtil.getINSTNCE().encode(data, (byte) 0x81);
        int send_num = buffer.capacity() / 20;
        byte[] data_send = new byte[20];
        buffer.position(0);
        for (int i = 0; i < send_num; i++) {
            buffer.get(data_send);
            write(data_send);
        }
    }

    /**
     * 拍照完成信息
     *
     * @param welderType  焊机型号
     * @param welderNum   焊机铭牌编号
     * @param requestCode 拍照请求序号
     * @param resultCode  拍照结果，1：焊接合格，2：焊接不合格
     * @param listener    监听
     */
    public static void onPhotoCaptured(
            String welderType,
            String welderNum,
            int requestCode,
            int resultCode,
            CmdParseExecute.PhotoFinishInfoListener listener
    ) {
        CmdParseExecute.getINSTNCE().setPhotoDataListener(listener);
        ByteBuffer buffer1 = ByteBuffer.allocate(22);
        buffer1.put(welderType.getBytes());
        buffer1.put(welderNum.getBytes());
        for (int i = 0; i < (14 - welderNum.length()); i++) {
            buffer1.put((byte) 0x20);
        }
        buffer1.put((byte) requestCode);
        buffer1.put((byte) resultCode);
        byte[] data = new byte[buffer1.capacity()];
        buffer1.position(0);
        buffer1.get(data);
        ByteBuffer buffer = EncodeUtil.getINSTNCE().encode(data, (byte) 0x82);
        int send_num = buffer.capacity() / 20;
        byte[] data_send = new byte[20];
        buffer.position(0);
        for (int i = 0; i < send_num; i++) {
            buffer.get(data_send);
            write(data_send);
        }
    }

    /**
     * 授权工程信息
     *
     * @param welderType 焊机类型
     * @param welderNum  焊机铭牌编号
     * @param projectNum 工程编号
     * @param listener   监听
     */
    public static void checkProjectInfo(
            String welderType,
            String welderNum,
            String projectNum,
            CmdParseExecute.ScanProjectInfoListener listener
    ) {
        CmdParseExecute.getINSTNCE().setScanDataListener(listener);
        ByteBuffer buffer1 = ByteBuffer.allocate(40);
        buffer1.put(welderType.getBytes());
        buffer1.put(welderNum.getBytes());
        for (int i = 0; i < (14 - welderNum.length()); i++) {
            buffer1.put((byte) 0x20);
        }
        if (TextUtils.isEmpty(projectNum)) {
            for (int i = 0; i < 20; i++) {
                buffer1.put((byte) 0x20);
            }
        } else {
            buffer1.put(projectNum.getBytes());
            for (int i = 0; i < (20 - projectNum.getBytes().length); i++) {
                if (buffer1.remaining() > 0) {
                    buffer1.put((byte) 0x20);
                }
            }
        }
        byte[] data = new byte[buffer1.capacity()];
        buffer1.position(0);
        buffer1.get(data);
        ByteBuffer buffer = EncodeUtil.getINSTNCE().encode(data, (byte) 0x83);
        int send_num = buffer.capacity() / 20;
        byte[] data_send = new byte[20];
        buffer.position(0);
        for (int i = 0; i < send_num; i++) {
            buffer.get(data_send);
            write(data_send);
        }
    }

    /**
     * 焊口编号验证
     *
     * @param welderType 焊机型号
     * @param welderNum  焊机铭牌编号
     * @param weldNum    焊口编号
     * @param result     验证结果，0：成功，1：失败
     * @param listener   监听
     */
    public static void checkWeldJointNumber(
            String welderType,
            String welderNum,
            String weldNum,
            int result,
            CmdParseExecute.CheckSpotNoListener listener
    ) {
        CmdParseExecute.getINSTNCE().setCheckSpotNoListener(listener);
        ByteBuffer buffer1 = ByteBuffer.allocate(41);
        buffer1.put(welderType.getBytes());
        buffer1.put(welderNum.getBytes());
        for (int i = 0; i < (14 - welderNum.length()); i++) {
            buffer1.put((byte) 0x20);
        }
        buffer1.put((byte) result);
        buffer1.put(weldNum.getBytes());
        for (int i = 0; i < (20 - weldNum.replace(" ", "").getBytes().length); i++) {
            if (buffer1.remaining() > 0) {
                buffer1.put((byte) 0x20);
            }
        }
        byte[] data = new byte[buffer1.capacity()];
        buffer1.position(0);
        buffer1.get(data);
        ByteBuffer buffer = EncodeUtil.getINSTNCE().encode(data, (byte) 0x71);
        int send_num = buffer.capacity() / 20;
        byte[] data_send = new byte[20];
        buffer.position(0);
        for (int i = 0; i < send_num; i++) {
            buffer.get(data_send);
            write(data_send);
        }
    }

    /**
     * 获取焊口焊接信息
     *
     * @param projectNum     项目编号
     * @param weldingPortNum 业务焊口号（唯一ID）
     * @param welderNum      焊机编号
     * @param listener       监听
     */
    public static void getWeldJointInfo(
            String projectNum,
            String weldingPortNum,
            String welderNum,
            CmdParseExecute.WeldInfoListener listener
    ) {
        CmdParseExecute.getINSTNCE().setDataListener(listener);

        ByteBuffer buffer1 = ByteBuffer.allocate(60);
        buffer1.put(projectNum.getBytes());
        for (int i = 0; i < (20 - projectNum.getBytes().length); i++) {
            if (buffer1.remaining() > 0) {
                buffer1.put((byte) 0x20);
            }
        }
        buffer1.put(weldingPortNum.getBytes());
        for (int i = 0; i < (20 - weldingPortNum.replace(" ", "").getBytes().length); i++) {
            if (buffer1.remaining() > 0) {
                buffer1.put((byte) 0x20);
            }
        }
        if (buffer1.remaining() > 0) {
            buffer1.put(welderNum.getBytes());
        }
        for (int i = 0; i < (20 - welderNum.getBytes().length); i++) {
            if (buffer1.remaining() > 0) {
                buffer1.put((byte) 0x20);
            }
        }
        byte[] data = new byte[buffer1.capacity()];
        buffer1.position(0);
        buffer1.get(data);
        ByteBuffer buffer = EncodeUtil.getINSTNCE().encode(data, (byte) 0x94);
        int send_num = buffer.capacity() / 20;
        byte[] data_send = new byte[20];
        buffer.position(0);

        for (int i = 0; i < send_num; i++) {
            buffer.get(data_send);
            write(data_send);
        }
    }

    private static void write(byte[] data) {
        writeCharacteristic.setValue(data);
        bluetoothGatt.writeCharacteristic(writeCharacteristic);
        LogUtils.d("App写出的数据", ConvertUtils.bytes2HexString(data), ConvertUtils.bytes2String(data));
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void onWeldNumberVerification(CmdParseExecute.CheckSpotNoListener listener) {
        CmdParseExecute.getINSTNCE().setCheckSpotNoListener(listener);
    }
}
