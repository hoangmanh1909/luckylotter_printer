package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;

@SuppressLint("ParcelCreator")
public class SearchOrderResponse extends SimpleResult {

    @SerializedName("ListValue")
    private List<OrderModel> orderModels;

    public List<OrderModel> getOrderModels() {
        return orderModels;
    }
}
