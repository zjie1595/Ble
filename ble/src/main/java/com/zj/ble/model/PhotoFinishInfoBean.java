package com.zj.ble.model;

public class PhotoFinishInfoBean extends OpenMachineInfoBean{
    private int photoCode;

    public int getPhotoCode() {
        return photoCode;
    }

    public void setPhotoCode(int photoCode) {
        this.photoCode = photoCode;
    }

    @Override
    public String toString() {
        return "PhotoFinishInfoBean{" +
                "photoCode=" + photoCode +
                '}';
    }
}
