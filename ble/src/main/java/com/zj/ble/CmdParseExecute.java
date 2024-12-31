package com.zj.ble;

import com.zj.ble.model.CheckVersionBean;
import com.zj.ble.model.CmdConstant;
import com.zj.ble.model.DecodeBean;
import com.zj.ble.model.LocationInfoBean;
import com.zj.ble.model.MachineConfigInfoBean;
import com.zj.ble.model.MachineDrConfigInfoBean;
import com.zj.ble.model.ModifyMachineInfoBean;
import com.zj.ble.model.OpenMachineInfoBean;
import com.zj.ble.model.PhotoFinishInfoBean;
import com.zj.ble.model.ScanBarCodeBean;
import com.zj.ble.model.SpotNoCheckBean;
import com.zj.ble.model.SpotWorkBean;
import com.zj.ble.model.WeldInfoBean;
import com.zj.ble.model.WeldJointInfoBean;
import com.zj.ble.model.WeldJointInfoDrBean;

import java.nio.ByteBuffer;

/**
 * Created by dhj on 2020/7/26.
 */
public class CmdParseExecute {
    public static CmdParseExecute INSTNCE;
    private WeldInfoListener mWeldInfoListener;
    private WeldInfoShowListener mWeldInfoShowListener;
    private StartWorkListener mStartWorkListener;
    private NotifyListener mNotifyListener;
    private OpenMachineInfoListener mOpenMachineInfoListener;
    private PhotoFinishInfoListener mPhotoFinishInfoListener;
    private ScanProjectInfoListener mScanProjectInfoListener;
    private ScanBarCodeListener mScanBarCodeListener;
    private CheckSpotNoListener mCheckSpotNoListener;
    private LocationInfoListener mLocationInfoListener;
    private ModifyMachineInfoListener mModifyMachineInfoListener;
    private MachineConfigInfoListener mMachineConfigInfoListener;
    private CheckVersionListener mCheckVersionListener;

    public void parse(DecodeBean decodeBean, int dataType) {

        switch (decodeBean.cmd) {

            case CmdConstant.WELD_INFO:
                parseWeldInfo(decodeBean);
                break;
            case CmdConstant.OPEN_MACHINE:
                parseOpenInfo(decodeBean);
                break;
            case CmdConstant.SCAN_PROJECT:
                parseProjectInfo(decodeBean);
                break;
            case CmdConstant.SCAN_CODE:
                parseBarCodeInfo(decodeBean);
                break;
            case CmdConstant.CHECK_SPOT_NO:
                parseCheckSpotNo(decodeBean);
                break;
            case CmdConstant.LOCATION_INFO:
                parseLocationInfo(decodeBean);
                break;
            case CmdConstant.FINISH_PHOTO:
                parsePhotoInfo(decodeBean);
                break;
            case CmdConstant.START_WORK:
                parseStartWork(decodeBean);
                break;
            case CmdConstant.HISTORY_LIST:
                parseHistory(decodeBean);
                break;
            case CmdConstant.SPOT_INFO:
                parseHankou(decodeBean, dataType);
                break;
            case CmdConstant.MODIFY_MACHINE:
                parseModify(decodeBean);
                break;
            case CmdConstant.CURRENT_CONFIGURATION:
                parseConfig(decodeBean, dataType);
                break;
            case CmdConstant.TAKE_PHOTO:
            case CmdConstant.FINISH_INFO:
                parseNotify(decodeBean);
                break;
            case CmdConstant.CHECK_VERSION:
                parseCheckVersion(decodeBean);
            default:
                break;
        }

    }

    private void parseCheckVersion(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        CheckVersionBean checkVersionBean = new CheckVersionBean();
        if (decodeBean.status == 0) {
            byte[] welderType = new byte[6];
            buffer.get(welderType);
            checkVersionBean.setWelderType(new String(welderType, 0, welderType.length).trim());
            byte[] welderNum = new byte[14];
            buffer.get(welderNum);
            checkVersionBean.setWelderNum(new String(welderNum, 0, welderNum.length).trim());
            byte[] versionArea = new byte[5];
            buffer.get(versionArea);
            checkVersionBean.setVersionArea(new String(versionArea, 0, versionArea.length).trim());
            byte[] versionName = new byte[10];
            buffer.get(versionName);
            checkVersionBean.setVersionName(new String(versionName, 0, versionName.length).trim());
            byte[] versionCode = new byte[4];
            buffer.get(versionCode);
            checkVersionBean.setVersionCode(Integer.parseInt(new String(versionCode, 0, versionCode.length).trim()));
            mCheckVersionListener.setCheckVersionData(checkVersionBean);
        } else {
            mCheckVersionListener.checkVersionError(decodeBean);
        }
    }

    private CmdParseExecute() {
    }

    public static CmdParseExecute getINSTNCE() {
        if (INSTNCE == null) {
            synchronized (CmdParseExecute.class) {
                INSTNCE = new CmdParseExecute();
            }
        }
        return INSTNCE;
    }

    private void parseOpenInfo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        OpenMachineInfoBean openMachineInfoBean = new OpenMachineInfoBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            openMachineInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            openMachineInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            mOpenMachineInfoListener.setOpenData(openMachineInfoBean);
        } else {
            mOpenMachineInfoListener.errorData(decodeBean);
        }
    }

    private void parseProjectInfo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        OpenMachineInfoBean openMachineInfoBean = new OpenMachineInfoBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            openMachineInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            openMachineInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            mScanProjectInfoListener.setProjectData(openMachineInfoBean);
        } else {
            mScanProjectInfoListener.errorData(decodeBean);
        }
    }

    private void parseBarCodeInfo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        ScanBarCodeBean scanBarCodeBean = new ScanBarCodeBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            scanBarCodeBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            scanBarCodeBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            mScanBarCodeListener.setScanCodeData(scanBarCodeBean);
        } else {
            mScanBarCodeListener.errorData(decodeBean);
        }
    }

    /**
     * 解析焊机返回焊口号验证的数据
     *
     * @param decodeBean
     */
    private void parseCheckSpotNo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        SpotNoCheckBean spotNoCheckBean = new SpotNoCheckBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            spotNoCheckBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            spotNoCheckBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            byte[] mSpotNumber = new byte[20];
            ByteBuffer buffer_hankou = buffer.get(mSpotNumber);

            String tempWeldingPortNum = new String(mSpotNumber, 0, mSpotNumber.length).replace(" ", "");
            spotNoCheckBean.setWelderPortNumber(tempWeldingPortNum);

            mCheckSpotNoListener.setSpotNoData(spotNoCheckBean);
        } else {
            mCheckSpotNoListener.errorData(decodeBean);
        }
    }

    /**
     * 解析焊机返回定位信息的数据
     *
     * @param decodeBean
     */
    private void parseLocationInfo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        LocationInfoBean locationInfoBean = new LocationInfoBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            locationInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            locationInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            byte[] mLongitude = new byte[10];
            ByteBuffer buffer_mLongitude = buffer.get(mLongitude);
            locationInfoBean.setLongitude(new String(mLongitude, 0, mLongitude.length));

            byte[] mLatitude = new byte[9];
            ByteBuffer buffer_mLatitude = buffer.get(mLatitude);
            locationInfoBean.setLatitude(new String(mLatitude, 0, mLatitude.length));

            mLocationInfoListener.setLocationData(locationInfoBean);
        } else {
            mLocationInfoListener.errorData(decodeBean);
        }
    }

    private void parsePhotoInfo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        PhotoFinishInfoBean photoFinishInfoBean = new PhotoFinishInfoBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            photoFinishInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            photoFinishInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            photoFinishInfoBean.setPhotoCode(buffer.get());

            mPhotoFinishInfoListener.setPhotoData(photoFinishInfoBean);
        } else {
            mPhotoFinishInfoListener.errorPhotoData(decodeBean);
        }
    }

    private void parseWeldInfo(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        WeldInfoBean weldInfoBean = new WeldInfoBean();

        if (decodeBean.status == 0) {
            byte[] mWelderNum = new byte[20];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderNum);
            weldInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            mWeldInfoShowListener.setWeldDataShow(weldInfoBean);
        } else {
            mWeldInfoShowListener.errorData(decodeBean);
        }
    }

    private void parseStartWork(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        SpotWorkBean spotWorkBean = new SpotWorkBean();

        if (decodeBean.status == 0) {
            byte[] mProjectNum = new byte[20];
            ByteBuffer buffer_ProjectNum = buffer.get(mProjectNum);
            spotWorkBean.setProjectNum(new String(mProjectNum, 0, mProjectNum.length).replace(" ", ""));

            byte[] mWeldingPortNum = new byte[20];
            ByteBuffer buffer_WeldingPortNum = buffer.get(mWeldingPortNum);
            spotWorkBean.setWeldingPortNum(new String(mWeldingPortNum, 0, mWeldingPortNum.length));

            byte[] mWelderNum = new byte[20];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderNum);
            spotWorkBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            spotWorkBean.setRespCode(buffer.get());

            mStartWorkListener.setWorkData(spotWorkBean);
        } else {
            mStartWorkListener.errorWorkData(decodeBean);
        }
    }


    private void parseHistory(DecodeBean decodeBean) {

    }

    public void parseHankou(DecodeBean decodeBean, int mDataType) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);

        if (decodeBean.status == 0) {
            if (mDataType == 2) {
                WeldJointInfoDrBean weldJointInfoDrBean = new WeldJointInfoDrBean();
                byte[] project_num = new byte[20];
                ByteBuffer buffer_project = buffer.get(project_num);
                weldJointInfoDrBean.setProjectNum(new String(project_num, 0, project_num.length).replace(" ", ""));
                byte[] project_hankou = new byte[20];
                ByteBuffer buffer_hankou = buffer.get(project_hankou);

                String tempWeldingPortNum = new String(project_hankou, 0, project_hankou.length).replace(" ", "");
                weldJointInfoDrBean.setWeldingPortNum(tempWeldingPortNum);

                weldJointInfoDrBean.setWeldingSerialNum(buffer.getShort());

                byte[] welderNum = new byte[20];
                ByteBuffer byteWelderNum = buffer.get(welderNum);
                weldJointInfoDrBean.setWelderNum(new String(welderNum, 0, welderNum.length).replace(" ", ""));

                weldJointInfoDrBean.setFittingType(buffer.get());

                weldJointInfoDrBean.setPipeDiameter(buffer.getShort());
                weldJointInfoDrBean.setSdr(buffer.getShort());

                byte[] mStartTime = new byte[19];
                ByteBuffer byteStartTime = buffer.get(mStartTime);
                weldJointInfoDrBean.setStartTime(new String(mStartTime, 0, mStartTime.length));

                weldJointInfoDrBean.setAmbientTemp(buffer.getShort());

                weldJointInfoDrBean.setNominalResistance(buffer.getShort());

                weldJointInfoDrBean.setMeasuredResistance(buffer.getShort());

                weldJointInfoDrBean.setWeldingVoltage(buffer.getShort());

                weldJointInfoDrBean.setWeldingActualVoltage(buffer.getShort());

                weldJointInfoDrBean.setHeat(buffer.getInt());

                weldJointInfoDrBean.setHeatingAdjustmentTimeValue(buffer.getShort());

                weldJointInfoDrBean.setHeatingSettingTimeValue(buffer.getShort());

                weldJointInfoDrBean.setCoolDownSettingTimeValue(buffer.getShort());

                weldJointInfoDrBean.setHeatingActualTimeValue(buffer.getShort());

                weldJointInfoDrBean.setCoolDownActualTimeValue(buffer.getShort());

                byte[] mEndTime = new byte[19];
                ByteBuffer byteEndTime = buffer.get(mEndTime);
                weldJointInfoDrBean.setEndTime(new String(mEndTime, 0, mEndTime.length));

                byte[] mStandards = new byte[14];
                ByteBuffer byteStandards = buffer.get(mStandards);
                weldJointInfoDrBean.setWeldingStandards(new String(mStandards, 0, mStandards.length));

                byte[] mGps = new byte[19];
                ByteBuffer byteGps = buffer.get(mGps);
                weldJointInfoDrBean.setGps(new String(mGps, 0, mGps.length));
                weldJointInfoDrBean.setWeldingState(buffer.get());
                mWeldInfoListener.setWeldDrData(weldJointInfoDrBean);
            } else {
                WeldJointInfoBean weldJointInfoBean = new WeldJointInfoBean();
                byte[] project_num = new byte[20];
                ByteBuffer buffer_project = buffer.get(project_num);
                weldJointInfoBean.setProjectNum(new String(project_num, 0, project_num.length).replace(" ", ""));
                byte[] project_hankou = new byte[20];
                ByteBuffer buffer_hankou = buffer.get(project_hankou);
                String tempWeldingPortNum = new String(project_hankou, 0, project_hankou.length).replace(" ", "");
                weldJointInfoBean.setWeldingPortNum(tempWeldingPortNum);

                weldJointInfoBean.setWeldingSerialNum(buffer.getShort());
                byte[] welderNum = new byte[20];
                ByteBuffer byteWelderNum = buffer.get(welderNum);
                weldJointInfoBean.setWelderNum(new String(welderNum, 0, welderNum.length).replace(" ", ""));

                weldJointInfoBean.setPe(buffer.get());
                weldJointInfoBean.setPipeDiameter(buffer.getShort());
                weldJointInfoBean.setSdr(buffer.getShort());

                byte[] mStartTime = new byte[19];
                ByteBuffer byteStartTime = buffer.get(mStartTime);
                weldJointInfoBean.setStartTime(new String(mStartTime, 0, mStartTime.length));

                weldJointInfoBean.setAmbientTemp(buffer.getShort());
                weldJointInfoBean.setPressureDrag(buffer.getShort());

                weldJointInfoBean.setHotTemp(buffer.getShort());
                weldJointInfoBean.setCrimpingSettingValue(buffer.getShort());
                weldJointInfoBean.setCrimpingSettingTimeValue(buffer.getShort());

                weldJointInfoBean.setCrimpingActualValue(buffer.getShort());
                weldJointInfoBean.setCrimpingActualTimeValue(buffer.getShort());

                weldJointInfoBean.setEndothermicSettingValue(buffer.getShort());
                weldJointInfoBean.setEndothermicSettingTimeValue(buffer.getShort());

                weldJointInfoBean.setEndothermicActualValue(buffer.getShort());
                weldJointInfoBean.setEndothermicActualTimeValue(buffer.getShort());

                weldJointInfoBean.setDockingRiseTime(buffer.getShort());
                weldJointInfoBean.setCoolDownSettingValue(buffer.getShort());

                weldJointInfoBean.setCoolDownSettingTimeValue(buffer.getShort());
                weldJointInfoBean.setCoolDownActualValue(buffer.getShort());
                weldJointInfoBean.setCoolDownActualTimeValue(buffer.getShort());

                byte[] mEndTime = new byte[19];
                ByteBuffer byteEndTime = buffer.get(mEndTime);
                weldJointInfoBean.setEndTime(new String(mEndTime, 0, mEndTime.length));

                byte[] mStandards = new byte[14];
                ByteBuffer byteStandards = buffer.get(mStandards);
                weldJointInfoBean.setWeldingStandards(new String(mStandards, 0, mStandards.length));

                byte[] mGps = new byte[19];
                ByteBuffer byteGps = buffer.get(mGps);
                weldJointInfoBean.setGps(new String(mGps, 0, mGps.length));

                weldJointInfoBean.setWeldingState(buffer.get());

                weldJointInfoBean.setCrimpingHeight(buffer.getShort());
                mWeldInfoListener.setWeldData(weldJointInfoBean);
            }
        } else if (decodeBean.status == 1) {
            // parse error
            mWeldInfoListener.error(decodeBean);
        } else {
            mWeldInfoListener.notify(decodeBean);
        }

    }

    private void parseModify(DecodeBean decodeBean) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        ModifyMachineInfoBean mModifyMachineInfoBean = new ModifyMachineInfoBean();

        if (decodeBean.status == 0) {
            byte[] mWelderType = new byte[6];
            ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
            mModifyMachineInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

            byte[] mWelderNum = new byte[14];
            ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
            mModifyMachineInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

            mModifyMachineInfoBean.setModifyResult(buffer.get());

            mModifyMachineInfoListener.setModifyData(mModifyMachineInfoBean);
        } else {
            mModifyMachineInfoListener.errorData(decodeBean);
        }
    }

    private void parseConfig(DecodeBean decodeBean, int mDataType) {
        ByteBuffer buffer = ByteBuffer.wrap(decodeBean.data);
        if (decodeBean.status == 0) {
            if (mDataType == 2) {
                MachineDrConfigInfoBean mMachineConfigInfoBean = new MachineDrConfigInfoBean();

                byte[] mWelderType = new byte[6];
                ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
                mMachineConfigInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

                byte[] mWelderNum = new byte[14];
                ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
                mMachineConfigInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

                mMachineConfigInfoBean.setMachineFlag(buffer.getShort());

                mMachineConfigInfoBean.setMachine1(buffer.get());
                mMachineConfigInfoBean.setMachine2(buffer.get());
                mMachineConfigInfoBean.setMachine3(buffer.get());
                mMachineConfigInfoBean.setMachine4(buffer.get());
                mMachineConfigInfoBean.setMachine5(buffer.get());

                mMachineConfigInfoBean.setMachine6(buffer.getShort());
                mMachineConfigInfoBean.setMachine7(buffer.getShort());

                mMachineConfigInfoBean.setMachine8(buffer.get());
                mMachineConfigInfoBean.setMachine9(buffer.get());
                mMachineConfigInfoBean.setMachine10(buffer.get());

                mMachineConfigInfoListener.setMachineConfigData(mMachineConfigInfoBean);
            } else {
                MachineConfigInfoBean mMachineRrConfigInfoBean = new MachineConfigInfoBean();

                byte[] mWelderType = new byte[6];
                ByteBuffer buffer_WelderNum = buffer.get(mWelderType);
                mMachineRrConfigInfoBean.setWelderType(new String(mWelderType, 0, mWelderType.length));

                byte[] mWelderNum = new byte[14];
                ByteBuffer buffer_WelderNum1 = buffer.get(mWelderNum);
                mMachineRrConfigInfoBean.setWelderNum(new String(mWelderNum, 0, mWelderNum.length));

                mMachineRrConfigInfoBean.setMachineFlag(buffer.getShort());

                mMachineRrConfigInfoBean.setMachine1(buffer.get());
                mMachineRrConfigInfoBean.setMachine2(buffer.get());
                mMachineRrConfigInfoBean.setMachine3(buffer.get());

                mMachineRrConfigInfoBean.setMachine4(buffer.get());
                mMachineRrConfigInfoBean.setMachine5(buffer.get());
                mMachineRrConfigInfoBean.setMachine6(buffer.get());

                mMachineRrConfigInfoBean.setMachine7(buffer.get());

                mMachineConfigInfoListener.setRrMachineConfigData(mMachineRrConfigInfoBean);
            }
        } else {
            mMachineConfigInfoListener.errorData(decodeBean);
        }
    }

    private void parseNotify(DecodeBean decodeBean) {
        if (decodeBean == null) {
            return;
        }
        if (decodeBean.status == 2) {
            mNotifyListener.notify(decodeBean);
        }
    }

    public interface OpenMachineInfoListener {
        void setOpenData(OpenMachineInfoBean openMachineInfoBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setOpenDataListener(OpenMachineInfoListener mOpenMachineInfoListener) {
        this.mOpenMachineInfoListener = mOpenMachineInfoListener;
    }

    public interface ScanProjectInfoListener {
        void setProjectData(OpenMachineInfoBean openMachineInfoBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setScanDataListener(ScanProjectInfoListener mScanProjectInfoListener) {
        this.mScanProjectInfoListener = mScanProjectInfoListener;
    }

    public interface ScanBarCodeListener {
        void setScanCodeData(ScanBarCodeBean scanBarCodeBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setScanBarCodeListener(ScanBarCodeListener mScanBarCodeListener) {
        this.mScanBarCodeListener = mScanBarCodeListener;
    }

    public interface CheckSpotNoListener {
        void setSpotNoData(SpotNoCheckBean spotNoCheckBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setCheckSpotNoListener(CheckSpotNoListener mCheckSpotNoListener) {
        this.mCheckSpotNoListener = mCheckSpotNoListener;
    }

    public interface LocationInfoListener {
        void setLocationData(LocationInfoBean locationInfoBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setLocationInfoListener(LocationInfoListener mLocationInfoListener) {
        this.mLocationInfoListener = mLocationInfoListener;
    }

    public interface PhotoFinishInfoListener {
        void setPhotoData(PhotoFinishInfoBean photoFinishInfoBean);

        void errorPhotoData(DecodeBean decodeBean);
    }

    public void setPhotoDataListener(PhotoFinishInfoListener mPhotoFinishInfoListener) {
        this.mPhotoFinishInfoListener = mPhotoFinishInfoListener;
    }

    public interface WeldInfoShowListener {
        void setWeldDataShow(WeldInfoBean weldInfoBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setDataShowListener(WeldInfoShowListener mWeldInfoShowListener) {
        this.mWeldInfoShowListener = mWeldInfoShowListener;
    }

    public interface StartWorkListener {
        void setWorkData(SpotWorkBean spotWorkBean);

        void errorWorkData(DecodeBean decodeBean);
    }

    public void setStartWorkListener(StartWorkListener mStartWorkListener) {
        this.mStartWorkListener = mStartWorkListener;
    }

    public interface WeldInfoListener {
        void setWeldData(WeldJointInfoBean decodeBean);

        void setWeldDrData(WeldJointInfoDrBean decodeBean);

        void error(DecodeBean decodeBean);

        void notify(DecodeBean decodeBean);
    }

    public void setDataListener(WeldInfoListener mWeldInfoListener) {
        this.mWeldInfoListener = mWeldInfoListener;
    }

    public interface NotifyListener {
        void notify(DecodeBean decodeBean);
    }

    public void setNotifyListener(NotifyListener mNotifyListener) {
        this.mNotifyListener = mNotifyListener;
    }

    public interface ModifyMachineInfoListener {
        void setModifyData(ModifyMachineInfoBean modifyMachineInfoBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setModifyDataListener(ModifyMachineInfoListener mModifyMachineInfoListener) {
        this.mModifyMachineInfoListener = mModifyMachineInfoListener;
    }

    public interface MachineConfigInfoListener {
        void setMachineConfigData(MachineDrConfigInfoBean machineConfigInfoBean);

        void setRrMachineConfigData(MachineConfigInfoBean machineRrConfigInfoBean);

        void errorData(DecodeBean decodeBean);
    }

    public void setMachineConfigDataListener(MachineConfigInfoListener mMachineConfigInfoListener) {
        this.mMachineConfigInfoListener = mMachineConfigInfoListener;
    }

    public interface CheckVersionListener {
        void setCheckVersionData(CheckVersionBean checkVersionBean);

        void checkVersionError(DecodeBean decodeBean);
    }

    public void setCheckVersionListener(CheckVersionListener listener) {
        mCheckVersionListener = listener;
    }
}
