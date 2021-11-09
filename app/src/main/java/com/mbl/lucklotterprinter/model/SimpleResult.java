package com.mbl.lucklotterprinter.model;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
@SuppressLint("ParcelCreator")
public class SimpleResult implements Parcelable {
    @SerializedName(value = "code", alternate = "Code")
    String errorCode = "-100";

    @SerializedName(value = "message", alternate = "Message")
    String message  ="";

    @Override
    public String toString() {
        return "SimpleResult{" +
                "errorCode=" + errorCode +
                ", message='" + message + '\'' +
                '}';
    }

//    @SerializedName("ListValue")
//    List<Object> objectList;
//
//    public List<Object> getObjectList() {
//        return objectList;
//    }
//
//    public void setObjectList(List<Object> objectList) {
//        this.objectList = objectList;
//    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SimpleResult that = (SimpleResult) o;

        if (!errorCode.equals(that.errorCode)) return false;
        return message.equals(that.message);

    }

    @Override
    public int hashCode() {
        int result = errorCode.hashCode();
        result = 31 * result + message.hashCode();
        return result;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.errorCode);
        dest.writeString(this.message);
    }

    public SimpleResult() {
    }

    protected SimpleResult(Parcel in) {
        this.errorCode = in.readString();
        this.message = in.readString();
    }
}
