package com.mbl.lucklotterprinter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrawModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("DrawCode")
    @Expose
    private Object drawCode;
    @SerializedName("DrawDate")
    @Expose
    private Object drawDate;
    @SerializedName("DrawTime")
    @Expose
    private Object drawTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Object getDrawCode() {
        return drawCode;
    }

    public void setDrawCode(Object drawCode) {
        this.drawCode = drawCode;
    }

    public Object getDrawDate() {
        return drawDate;
    }

    public void setDrawDate(Object drawDate) {
        this.drawDate = drawDate;
    }

    public Object getDrawTime() {
        return drawTime;
    }

    public void setDrawTime(Object drawTime) {
        this.drawTime = drawTime;
    }
}
