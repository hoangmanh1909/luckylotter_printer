package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.ItemModel;
import com.mbl.lucklotterprinter.model.OrderModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

import java.util.List;


@SuppressLint("ParcelCreator")
public class GetItemByCodeResponse extends SimpleResult {

    @SerializedName("Value")
    private String printCode;
    @SerializedName("ListValue")
    private List<ItemModel> itemModels;

    public List<ItemModel> getItemModels() {
        return itemModels;
    }

    public String getPrintCode() {
        return printCode;
    }
}
