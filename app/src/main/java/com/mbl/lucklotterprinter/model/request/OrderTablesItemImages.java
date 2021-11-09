package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderTablesItemImages {
    @SerializedName("orderItemID")
    @Expose
    private String orderItemID;
    @SerializedName("afterImages")
    @Expose
    private String afterImages;
    @SerializedName("beforeImages")
    @Expose
    private String beforeImages;

    public String getOrderItemID() {
        return orderItemID;
    }

    public void setOrderItemID(String orderItemID) {
        this.orderItemID = orderItemID;
    }

    public String getAfterImages() {
        return afterImages;
    }

    public void setAfterImages(String afterImages) {
        this.afterImages = afterImages;
    }

    public String getBeforeImages() {
        return beforeImages;
    }

    public void setBeforeImages(String beforeImages) {
        this.beforeImages = beforeImages;
    }
}
