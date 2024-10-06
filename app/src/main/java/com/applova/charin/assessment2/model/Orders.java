package com.applova.charin.assessment2.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Orders {

    @SerializedName("orderNum")
    @Expose
    private long orderNum;
    @SerializedName("products")
    @Expose
    private List<Product> products;
    @SerializedName("waiter")
    @Expose
    private String waiter;
    @SerializedName("subTotal")
    @Expose
    private double subTotal;
    @SerializedName("total")
    @Expose
    private double total;
    @SerializedName("taxes")
    @Expose
    private int taxes;
    @SerializedName("serviceCharge")
    @Expose
    private int serviceCharge;
    @SerializedName("discount")
    @Expose
    private int discount;

    public Orders() {
    }

    public Orders(long orderNum, List<Product> products, String waiter, double subTotal, double total, int taxes, int serviceCharge, int discount) {
        this.orderNum = orderNum;
        this.products = products;
        this.waiter = waiter;
        this.subTotal = subTotal;
        this.total = total;
        this.taxes = taxes;
        this.serviceCharge = serviceCharge;
        this.discount = discount;
    }

    public long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(long orderNum) {
        this.orderNum = orderNum;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getTaxes() {
        return taxes;
    }

    public void setTaxes(int taxes) {
        this.taxes = taxes;
    }

    public int getServiceCharge() {
        return serviceCharge;
    }

    public void setServiceCharge(int serviceCharge) {
        this.serviceCharge = serviceCharge;
    }

    public int getDiscount() {
        return discount;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        return "Orders{" +
                "orderNum=" + orderNum +
                ", products=" + products +
                ", waiter='" + waiter + '\'' +
                ", subTotal=" + subTotal +
                ", total=" + total +
                ", taxes=" + taxes +
                ", serviceCharge=" + serviceCharge +
                ", discount=" + discount +
                '}';
    }
}
