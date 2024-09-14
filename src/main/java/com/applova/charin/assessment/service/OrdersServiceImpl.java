package com.applova.charin.assessment.service;

import com.applova.charin.assessment.dao.OrdersRepository;
import com.applova.charin.assessment.dto.OrdersRequestDTO;
import com.applova.charin.assessment.dto.OrdersResponseDTO;
import com.applova.charin.assessment.utilities.OrdersException;
import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;


@Service
public class OrdersServiceImpl implements OrdersService {
    @Autowired
    private OrdersRepository ordersRepository;

    /**
     * Returns an OrdersResponseDTO which contains page content and page metadata,
     * metadata includes [Total Orders, Current Page, Orders per page, Total Pages]
     * with page size and page number of parameters
     * @param page The page number returned
     * @param size The total number of elements to be shown per page
     * @return Page<Orders> A page
     * @throws OrdersException Will be thrown when there is an Order specific exception
     */
    @Override
    public OrdersResponseDTO getOrders(int page, int size) throws OrdersException{
        OrdersResponseDTO response;
        Page<Orders> pageList;
        try{
            pageList = ordersRepository.findAll(PageRequest.of(page, size));

        } catch (DataAccessException ex){
            throw new OrdersException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        validateGetOrder(pageList, page, size);
        response = OrdersResponseDTO.builder()
                .content(pageList.getContent())
                .totalOrders(pageList.getTotalElements())
                .currentPage(pageList.getNumber())
                .ordersPerPage(pageList.getSize())
                .totalPages(pageList.getTotalPages())
                .build();
        return response;
    }

    /**
     * Will create a new order with data given in parameter
     * @param orderRequest The data to create the order with
     * @return Will return the created order, including the unique number of the created order
     * @throws OrdersException Will be thrown when there is an Order specific exception
     */
    @Override
    public Orders createOrder(OrdersRequestDTO orderRequest) throws OrdersException{
        Orders order = Orders.builder()
                        .products(orderRequest.getProducts())
                        .waiter(orderRequest.getWaiter())
                        .discount(orderRequest.getDiscount())
                        .serviceCharge(orderRequest.getServiceCharge())
                        .taxes(orderRequest.getTaxes()).build();
        calculateTotals(order);
        Orders successOrder;
        try{
            successOrder= ordersRepository.save(order);
        } catch (DataAccessException ex){
            throw new OrdersException(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage());
        }
        return successOrder;
    }

    /**
     * Validate the get order request from user
     * @param pageList The page List received from the repository
     * @param page The page number to be shown
     * @param size The total elements per page
     * @throws OrdersException Will be thrown when there is a validation error
     */
    private void validateGetOrder(Page<Orders> pageList, int page, int size) throws OrdersException {
        if(pageList.isEmpty())
            throw new OrdersException(HttpStatus.NO_CONTENT, "No Orders have been made yet");
    }

    /**
     * To calculate the totals and set them in the Order object
     * @param order The order which contains the prices and other
     *              charges to calculate and store total prices
     */
    private void calculateTotals(Orders order) {
        double subTotal = 0;
        for (Product product : order.getProducts()) {
            subTotal = subTotal + (product.getPrice() * product.getQuantity());
        }
        order.setSubTotal(subTotal);

        // Calculate the total by subtracting the discounted amount and then adding other charges
        double total = (subTotal -= (subTotal * order.getDiscount() / 100)) +
                (subTotal * order.getServiceCharge() / 100) +
                (subTotal * order.getTaxes() / 100);
        order.setTotal(total);
    }

}
