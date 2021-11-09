package com.mbl.lucklotterprinter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ParamsModel {
    @SerializedName("ID")
    @Expose
    private Integer id;
    @SerializedName("GroupCode")
    @Expose
    private String groupCode;
    @SerializedName("Parameter")
    @Expose
    private String parameter;
    @SerializedName("Value")
    @Expose
    private String value;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupCode() {
        return groupCode;
    }

    public void setGroupCode(String groupCode) {
        this.groupCode = groupCode;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
