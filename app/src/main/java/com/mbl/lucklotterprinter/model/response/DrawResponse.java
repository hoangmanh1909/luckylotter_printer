package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.DrawModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;

@SuppressLint("ParcelCreator")
public class DrawResponse extends SimpleResult {

    @SerializedName("ListValue")
    private List<DrawModel> drawModels;

    public List<DrawModel> getDrawModels() {
        return drawModels;
    }
}
