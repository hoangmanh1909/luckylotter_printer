package com.mbl.lucklotterprinter.model.request;

import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;

public class BaseRequest {
    @SerializedName("Channel")
    private String channel;
    @SerializedName("Version")
    private String version;
    @SerializedName("Code")
    private String code;
    @SerializedName("Data")
    private String data;

    public BaseRequest(String channel, String version, String code, String data) {
        if (!TextUtils.isEmpty(channel))
            this.channel = channel;
        else
            this.channel = "ANDROID";
        this.version = version;
        this.code = code;
        this.data = data;
    }
}
