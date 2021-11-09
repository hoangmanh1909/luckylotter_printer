package com.mbl.lucklotterprinter.model.response;

import android.annotation.SuppressLint;

import com.google.gson.annotations.SerializedName;
import com.mbl.lucklotterprinter.model.EmployeeModel;
import com.mbl.lucklotterprinter.model.SimpleResult;

@SuppressLint("ParcelCreator")
public class LoginResponse extends SimpleResult {
    @SerializedName("Value")
    private EmployeeModel employeeModel;

    public EmployeeModel getEmployeeModel() {
        return employeeModel;
    }

}
