package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.ParamsModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;

@SuppressLint("ParcelCreator")
public class ParamsResponse  extends SimpleResult {

    @SerializedName("ListValue")
    private List<ParamsModel> paramsModels;

    public List<ParamsModel> getParamsModels() {
        return paramsModels;
    }
}
