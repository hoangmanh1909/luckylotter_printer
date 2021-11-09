package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.SerializedName;

public class LoginRequest {
    @SerializedName("MobileNumber")
    private String mobileNumber;
    @SerializedName("Password")
    private String password;
    @SerializedName("DeviceCode")
    private String deviceCode;

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDeviceCode() {
        return deviceCode;
    }

    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
}
