package com.mbl.lucklotterprinter.model.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangeUpImageRequest {
    @SerializedName("Code")
    @Expose
    private Object code;
    @SerializedName("PrintCode")
    @Expose
    private Object printCode;

    public Object getCode() {
        return code;
    }

    public void setCode(Object code) {
        this.code = code;
    }

    public Object getPrintCode() {
        return printCode;
    }

    public void setPrintCode(Object printCode) {
        this.printCode = printCode;
    }

}
