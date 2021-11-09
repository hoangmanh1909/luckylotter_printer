package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OutOfNumberModel {
    @SerializedName("LineNo")
    @Expose
    private String lineNo;
    @SerializedName("OrderItemID")
    @Expose
    private String orderItemId;

    public void setLineNo(String lineNo) {
        this.lineNo = lineNo;
    }

    public void setOrderItemId(String orderItemId) {
        this.orderItemId = orderItemId;
    }

    public String getLineNo() {
        return lineNo;
    }

    public String getOrderItemId() {
        return orderItemId;
    }
}
