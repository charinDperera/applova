package com.applova.charin.assessment.dto;

import com.applova.charin.assessment.model.Orders;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class OrdersResponseDTO {
    private List<Orders> content;

    private Long totalOrders;

    private int currentPage;

    private int ordersPerPage;

    private int totalPages;
}
