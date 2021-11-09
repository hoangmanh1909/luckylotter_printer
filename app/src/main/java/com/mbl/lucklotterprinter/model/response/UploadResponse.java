package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.FileInfoModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;

@SuppressLint("ParcelCreator")
public class UploadResponse extends SimpleResult {
    @SerializedName("ListValue")
    private List<FileInfoModel> fileInfoModels;

    public List<FileInfoModel> getFileInfoModels() {
        return fileInfoModels;
    }
}
