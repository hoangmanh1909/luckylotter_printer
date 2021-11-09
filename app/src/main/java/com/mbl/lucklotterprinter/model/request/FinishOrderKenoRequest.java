package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FinishOrderKenoRequest {
    @SerializedName("OrderCode")
    @Expose
    private Object orderCode;
    @SerializedName("KenoImg")
    @Expose
    private Object kenoImg;
    @SerializedName("POSID")
    @Expose
    private Integer posid;
    @SerializedName("PrintCode")
    @Expose
    private Object printCode;

    public Object getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(Object orderCode) {
        this.orderCode = orderCode;
    }

    public Object getKenoImg() {
        return kenoImg;
    }

    public void setKenoImg(Object kenoImg) {
        this.kenoImg = kenoImg;
    }

    public Integer getPosid() {
        return posid;
    }

    public void setPosid(Integer posid) {
        this.posid = posid;
    }

    public Object getPrintCode() {
        return printCode;
    }

    public void setPrintCode(Object printCode) {
        this.printCode = printCode;
    }
}
