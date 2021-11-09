package com.mbl.lucklotterprinter.model;

import com.google.gson.annotations.SerializedName;

public class ImageModel {
    @SerializedName("FileName")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
