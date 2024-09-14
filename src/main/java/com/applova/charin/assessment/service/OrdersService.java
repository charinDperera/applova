package com.applova.charin.assessment.service;

import com.applova.charin.assessment.dto.OrdersRequestDTO;
import com.applova.charin.assessment.dto.OrdersResponseDTO;
import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.utilities.OrdersException;
import org.springframework.data.domain.Page;

public interface OrdersService {

    /**
     * Returns an OrdersResponseDTO which contains page content and page metadata,
     * metadata includes [Total Orders, Current Page, Orders per page, Total Pages]
     * with page size and page number of parameters
     * @param page The page number returned
     * @param size The total number of elements to be shown per page
     * @return Page<Orders> A page
     * @throws OrdersException Will be thrown when there is an Order specific exception
     */
    OrdersResponseDTO getOrders(int page, int size) throws OrdersException;

    /**
     * Will create a new order with data given in parameter
     * @param orderRequest The data to create the order with
     * @return Will return the created order, including the unique number of the created order
     * @throws OrdersException Will be thrown when there is an Order specific exception
     */
    Orders createOrder(OrdersRequestDTO orderRequest) throws OrdersException;
}
