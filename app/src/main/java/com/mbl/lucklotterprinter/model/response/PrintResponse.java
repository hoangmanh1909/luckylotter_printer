package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.PrintCommandModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;


@SuppressLint("ParcelCreator")
public class PrintResponse extends SimpleResult {
    @SerializedName("Value")
    private PrintCommandModel printCommandModel;

    public PrintCommandModel getPrintCommandModel() {
        return printCommandModel;
    }

    public void setPrintCommandModel(PrintCommandModel printCommandModel) {
        this.printCommandModel = printCommandModel;
    }
}
