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
BleUtils.getWelderInformation()
    
// 授权人员信息
BleUtils.checkOperatorInfo()
    
// 拍照完成信息
BleUtils.checkOperatorInfo()
    
// 授权工程信息
BleUtils.checkProjectInfo()
    
// 焊口编号验证
BleUtils.checkWeldJointNumber()
    
// 获取焊口焊接信息
BleUtils.getWeldJointInfo()
```

# 处理焊机通知

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

