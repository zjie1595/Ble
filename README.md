### BLE框架集成与使用文档

---

### 1. **安装**

#### Project 的 `settings.gradle` 添加仓库
```java
dependencyResolutionManagement {
    repositories {
        // 其他仓库...
        maven { url 'https://jitpack.io' }
    }
}
```

#### Module 的 `build.gradle` 添加依赖框架
```java
dependencies {
    implementation 'com.github.zjie1595:Ble:1.1.4'
}
```

---

### 2. **初始化**

#### 蓝牙连接成功时调用
```java
// 热熔焊机 type 传 1，电熔焊机 type 传 2
BleUtils.onBleConnected(type, gatt);
```

#### 接收到焊机发送的数据时调用
```java
BleUtils.onCharacteristicChanged(data);
```

#### 处理通知
```java
BleUtils.setNotifyListener(new BleUtils.Callback() {
    @Override
    public void takePhoto(DecodeBean decodeBean) {
        // 焊机请求拍照
    }

    @Override
    public void weldingFinish(DecodeBean decodeBean) {
        // 焊接完成
    }
});
```

#### 焊口编号验证
```java
BleUtils.onWeldNumberVerification(new CmdParseExecute.CheckSpotNoListener() {
    @Override
    public void setSpotNoData(SpotNoCheckBean spotNoCheckBean) {
        // 焊口编号验证成功
    }

    @Override
    public void errorData(DecodeBean decodeBean) {
        // 焊口编号验证失败
    }
});
```

---

### 3. **业务接口**

#### **3.1 获取焊机信息**
```java
BleUtils.getWelderInformation(new CmdParseExecute.WeldInfoShowListener() {
    @Override
    public void setWeldDataShow(WeldInfoBean weldInfoBean) {
        // 获取焊机信息成功
    }

    @Override
    public void errorData(DecodeBean decodeBean) {
        // 获取焊机信息失败
    }
});
```

#### **3.2 授权人员信息**
```java
BleUtils.checkOperatorInfo(
    "焊机型号",
    "焊机铭牌编号",
    "焊机操作员编号",
    new CmdParseExecute.OpenMachineInfoListener() {
        @Override
        public void setOpenData(OpenMachineInfoBean openMachineInfoBean) {
            // 授权人员信息成功
        }

        @Override
        public void errorData(DecodeBean decodeBean) {
            // 授权人员信息失败
        }
    }
);
```

#### **3.3 拍照完成信息**
```java
BleUtils.onPhotoCaptured(
    "焊机型号",
    "焊机铭牌编号",
    拍照请求序号, // 示例：10001
    拍照结果（1：焊接合格 2：焊接不合格）, // 示例：1
    new CmdParseExecute.PhotoFinishInfoListener() {
        @Override
        public void setPhotoData(PhotoFinishInfoBean photoFinishInfoBean) {
            // 拍照完成信息发送成功
        }

        @Override
        public void errorPhotoData(DecodeBean decodeBean) {
            // 拍照完成信息发送失败
        }
    }
);
```

#### **3.4 授权工程信息**
```java
BleUtils.checkProjectInfo(
    "焊机型号",
    "焊机铭牌编号",
    "工程编号",
    new CmdParseExecute.ScanProjectInfoListener() {
        @Override
        public void setProjectData(OpenMachineInfoBean openMachineInfoBean) {
            // 授权工程信息成功
        }

        @Override
        public void errorData(DecodeBean decodeBean) {
            // 授权工程信息失败
        }
    }
);
```

#### **3.5 焊口编号验证**
```java
BleUtils.checkWeldJointNumber(
    "焊机型号",
    "焊机铭牌编号",
    "焊口编号",
    验证结果（0：成功 1：失败）, // 示例：0
    new CmdParseExecute.CheckSpotNoListener() {
        @Override
        public void setSpotNoData(SpotNoCheckBean spotNoCheckBean) {
            // 焊口编号验证成功
        }

        @Override
        public void errorData(DecodeBean decodeBean) {
            // 焊口编号验证失败
        }
    }
);
```

#### **3.6 获取焊口焊接信息**
```java
BleUtils.getWeldJointInfo(
    "工程编号",
    "焊口编号",
    "焊机编号",
    new CmdParseExecute.WeldInfoListener() {
        @Override
        public void setWeldData(WeldJointInfoBean decodeBean) {
            // 获取焊口焊接信息成功（热熔）
        }

        @Override
        public void setWeldDrData(WeldJointInfoDrBean decodeBean) {
            // 获取焊口焊接信息成功（电熔）
        }

        @Override
        public void error(DecodeBean decodeBean) {
            // 获取焊口焊接信息失败
        }

        @Override
        public void notify(DecodeBean decodeBean) {
            // 其他通知信息
        }
    }
);
```
