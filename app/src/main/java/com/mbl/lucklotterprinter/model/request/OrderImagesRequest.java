package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.ImageModel;

import java.util.List;

public class OrderImagesRequest {
    @SerializedName("orderCode")
    private String orderCode;
    @SerializedName("pointOfSaleID")
    private Integer pointOfSaleID;
    @SerializedName("drawCode")
    private List<String> drawCode;
    @SerializedName("imageBefore")
    private List<ImageModel> imageBefore;
    @SerializedName("imageAfter")
    private List<ImageModel> imageAfter;

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

    public List<ImageModel> getImageBefore() {
        return imageBefore;
    }

    public void setImageBefore(List<ImageModel> imageBefore) {
        this.imageBefore = imageBefore;
    }

    public List<ImageModel> getImageAfter() {
        return imageAfter;
    }

    public void setImageAfter(List<ImageModel> imageAfter) {
        this.imageAfter = imageAfter;
    }
}
