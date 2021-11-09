package com.mbl.lucklotterprinter.model;

import com.google.gson.annotations.SerializedName;

public class FileInfoModel {
    @SerializedName("Type")
    private String type;
    @SerializedName("Name")
    private String name;
    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
