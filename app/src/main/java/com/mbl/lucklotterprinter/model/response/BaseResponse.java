package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;


@SuppressLint("ParcelCreator")
public class BaseResponse extends SimpleResult {
    @SerializedName("Value")
    private Object value;

    public Object getValue() {
        return value;
    }
}

