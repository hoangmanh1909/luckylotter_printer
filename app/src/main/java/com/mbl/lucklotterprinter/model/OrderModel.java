package com.mbl.lucklotterprinter.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class OrderModel implements Serializable {

    @SerializedName("OrderCode")
    @Expose
    private String orderCode;
    @SerializedName("Quantity")
    @Expose
    private int quantity;
    @SerializedName("ProductID")
    @Expose
    private int productID;
    @SerializedName("ProductName")
    @Expose
    private String productName;
    @SerializedName("Price")
    @Expose
    private long price;
    @SerializedName("Fee")
    @Expose
    private int fee;
    @SerializedName("Amount")
    @Expose
    private long amount;
    @SerializedName("OrderDate")
    @Expose
    private String orderDate;
    @SerializedName("Status")
    @Expose
    private String status;
    @SerializedName("PointOfSaleId")
    @Expose
    private int pointOfSaleId;
    @SerializedName("PointOfSaleName")
    @Expose
    private String pointOfSaleName;
    @SerializedName("FullName")
    @Expose
    private String fullName;
    @SerializedName("PIDNumber")
    @Expose
    private String pIDNumber;
    @SerializedName("MobileNumber")
    @Expose
    private String mobileNumber;
    @SerializedName("EmailAddress")
    @Expose
    private String emailAddress;
    @SerializedName("TicketCategory")
    @Expose
    private int ticketCategory;

    public int getTicketCategory() {
        return ticketCategory;
    }

    public void setTicketCategory(int ticketCategory) {
        this.ticketCategory = ticketCategory;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public long getPrice() {
        return price;
    }

    public void setPrice(long price) {
        this.price = price;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPointOfSaleId() {
        return pointOfSaleId;
    }

    public void setPointOfSaleId(int pointOfSaleId) {
        this.pointOfSaleId = pointOfSaleId;
    }

    public String getPointOfSaleName() {
        return pointOfSaleName;
    }

    public void setPointOfSaleName(String pointOfSaleName) {
        this.pointOfSaleName = pointOfSaleName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getpIDNumber() {
        return pIDNumber;
    }

    public void setpIDNumber(String pIDNumber) {
        this.pIDNumber = pIDNumber;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }
}
