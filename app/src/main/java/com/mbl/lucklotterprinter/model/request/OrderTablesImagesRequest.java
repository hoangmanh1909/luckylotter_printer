package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.ImageModel;

import java.util.List;

public class OrderTablesImagesRequest {
    @SerializedName("orderCode")
    private String orderCode;
    @SerializedName("pointOfSaleID")
    private Integer pointOfSaleID;
    @SerializedName("drawCode")
    private List<String> drawCode;
    @SerializedName("Items")
    private List<OrderTablesItemImages> items;

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getPointOfSaleID() {
        return pointOfSaleID;
    }

    public void setPointOfSaleID(Integer pointOfSaleID) {
        this.pointOfSaleID = pointOfSaleID;
    }

    public List<String> getDrawCode() {
        return drawCode;
    }

    public void setDrawCode(List<String> drawCode) {
        this.drawCode = drawCode;
    }

    public List<OrderTablesItemImages> getItems() {
        return items;
    }

    public void setItems(List<OrderTablesItemImages> items) {
        this.items = items;
    }
}
