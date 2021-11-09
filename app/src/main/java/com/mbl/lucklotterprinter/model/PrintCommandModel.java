package com.mbl.lucklotterprinter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class PrintCommandModel {
    @SerializedName("CommandText")
    @Expose
    private String commandText;
    @SerializedName("CurrentCode")
    @Expose
    private String currentCode;
    @SerializedName("CurrentDate")
    @Expose
    private Integer currentDate;
    @SerializedName("CurrentTime")
    @Expose
    private Integer currentTime;

    public String getCommandText() {
        return commandText;
    }

    public void setCommandText(String commandText) {
        this.commandText = commandText;
    }

    public String getCurrentCode() {
        return currentCode;
    }

    public void setCurrentCode(String currentCode) {
        this.currentCode = currentCode;
    }

    public Integer getCurrentDate() {
        return currentDate;
    }

    public void setCurrentDate(Integer currentDate) {
        this.currentDate = currentDate;
    }

    public Integer getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Integer currentTime) {
        this.currentTime = currentTime;
    }
}
