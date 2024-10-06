package com.applova.charin.assessment2.dto;

import com.applova.charin.assessment2.model.Orders;
import com.applova.charin.assessment2.model.Product;

import java.util.List;

public class CreateOrdersRequestDTO {

    private List<Product> products;

    private String waiter;

    private int taxes;

    private int serviceCharge;

    private int discount;

    public CreateOrdersRequestDTO(List<Product> products, String waiter, int taxes, int serviceCharge, int discount) {
        this.products = products;
        this.waiter = waiter;
        this.taxes = taxes;
        this.serviceCharge = serviceCharge;
        this.discount = discount;
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
        return "CreateOrdersRequestDTO{" +
                "products=" + products +
                ", waiter='" + waiter + '\'' +
                ", taxes=" + taxes +
                ", serviceCharge=" + serviceCharge +
                ", discount=" + discount +
                '}';
    }
}
