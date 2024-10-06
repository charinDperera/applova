package com.applova.charin.assessment2.dto;

import com.applova.charin.assessment2.model.Orders;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetOrdersResponseDTO {

    @SerializedName("content")
    @Expose
    private List<Orders> content;

    @SerializedName("totalOrders")
    @Expose
    private Long totalOrders;

    @SerializedName("currentPage")
    @Expose
    private int currentPage;

    @SerializedName("ordersPerPage")
    @Expose
    private int ordersPerPage;

    @SerializedName("totalPages")
    @Expose
    private int totalPages;

    public List<Orders> getContent() {
        return content;
    }

    public void setContent(List<Orders> content) {
        this.content = content;
    }

    public Long getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(Long totalOrders) {
        this.totalOrders = totalOrders;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getOrdersPerPage() {
        return ordersPerPage;
    }

    public void setOrdersPerPage(int ordersPerPage) {
        this.ordersPerPage = ordersPerPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    @Override
    public String toString() {
        return "GetOrdersResponseDTO{" +
                "content=" + content +
                ", totalOrders=" + totalOrders +
                ", currentPage=" + currentPage +
                ", ordersPerPage=" + ordersPerPage +
                ", totalPages=" + totalPages +
                '}';
    }
}
