# 安装

Project 的 settings.gradle 添加仓库
```java
dependencyResolutionManagement {
    repositories {
        // ...
        maven { url 'https://jitpack.io' }
    }
}
```
Module 的 build.gradle 添加依赖框架
```java
dependencies {
    implementation 'com.github.zjie1595:Ble:1.1.4'
}
```

# 初始化

```java
// 蓝牙连接成功时调用
// 热熔焊机type传1，电熔焊机type传2
BleUtils.onBleConnected(type,gatt);
```

```java
// 接收到焊机发送的数据时调用
BleUtils.onCharacteristicChanged(data);
```

处理通知
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

BleUtils.onWeldNumberVerification(new CmdParseExecute.CheckSpotNoListener() {
	@Override
	public void setSpotNoData(SpotNoCheckBean spotNoCheckBean) {
		// 焊口编号验证
	}

	@Override
	public void errorData(DecodeBean decodeBean) {
		// 焊口编号验证失败
	}
});
```
# 业务接口

```java
        // 获取焊机信息
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
        // 授权人员信息
        BleUtils.checkOperatorInfo("焊机型号", "焊机铭牌编号", "焊机操作员编号", new CmdParseExecute.OpenMachineInfoListener() {
            @Override
            public void setOpenData(OpenMachineInfoBean openMachineInfoBean) {
                // 授权人员信息成功
            }

            @Override
            public void errorData(DecodeBean decodeBean) {
                // 授权人员信息失败
            }
        });
        // 拍照完成信息
        BleUtils.onPhotoCaptured(
                "焊机型号",
                "焊机铭牌编号",
                拍照请求序号, 拍照结果（1：焊接合格 2：焊接不合格）,
                new CmdParseExecute.PhotoFinishInfoListener() {
                    @Override
                    public void setPhotoData(PhotoFinishInfoBean photoFinishInfoBean) {
                        // 拍照完成信息发送成功
                    }

                    @Override
                    public void errorPhotoData(DecodeBean decodeBean) {
                        // 拍照完成信息发送失败
                    }
                });
        // 授权工程信息
        BleUtils.checkProjectInfo("焊机型号", "焊机铭牌编号", "工程编号", new CmdParseExecute.ScanProjectInfoListener() {
            @Override
            public void setProjectData(OpenMachineInfoBean openMachineInfoBean) {
                // 授权工程信息成功
            }

            @Override
            public void errorData(DecodeBean decodeBean) {
                // 授权工程信息失败
            }
        });
        // 焊口编号验证
        BleUtils.checkWeldJointNumber(
                "焊机型号",
                "焊机铭牌编号",
                "焊口编号",
                验证结果（0：成功 1：失败）,
                new CmdParseExecute.CheckSpotNoListener() {
                    @Override
                    public void setSpotNoData(SpotNoCheckBean spotNoCheckBean) {
                        // 焊口编号验证成功
                    }

                    @Override
                    public void errorData(DecodeBean decodeBean) {
                        // 焊口编号验证失败
                    }
                });
        // 获取焊口焊接信息
        BleUtils.getWeldJointInfo("工程编号", "焊口编号", "焊机编号", new CmdParseExecute.WeldInfoListener() {
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

            }
        });
```

