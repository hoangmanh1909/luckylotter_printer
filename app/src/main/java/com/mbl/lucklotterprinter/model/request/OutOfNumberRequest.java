package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class OutOfNumberRequest {
    @SerializedName("OrderCode")
    @Expose
    private String orderCode;
    @SerializedName("ListNumber")
    @Expose
    private List<OutOfNumberModel> outOfNumberModelList;

    public List<OutOfNumberModel> getOutOfNumberModelList() {
        return outOfNumberModelList;
    }

    public void setOutOfNumberModelList(List<OutOfNumberModel> outOfNumberModelList) {
        this.outOfNumberModelList = outOfNumberModelList;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }
}
