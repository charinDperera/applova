package com.applova.charin.assessment.service;

import com.applova.charin.assessment.dao.OrdersRepository;
import com.applova.charin.assessment.dto.OrdersRequestDTO;
import com.applova.charin.assessment.dto.OrdersResponseDTO;
import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.model.Product;
import com.applova.charin.assessment.utilities.OrdersException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class OrdersServiceImplTest {

    @Mock
    private OrdersRepository ordersRepository;

    @InjectMocks
    private OrdersServiceImpl ordersServiceImpl;

    private OrdersRequestDTO createOrder;
    private Orders mockOrderResponse;

    @BeforeEach
    public void init(){
        createOrder = OrdersRequestDTO.builder()
                .products(List.of(
                        Product.builder()
                                .productName("Rice")
                                .quantity(5)
                                .price(100.00)
                                .build()
                ))
                .waiter("Saman")
                .build();

        mockOrderResponse = Orders.builder()
                .orderNum(1L)
                .products(List.of(
                        Product.builder()
                                .id(1L)
                                .productName("Rice")
                                .quantity(5)
                                .price(100.00)
                                .build()
                ))
                .subTotal(500.00)
                .total(500.00)
                .waiter("Saman")
                .taxes(0)
                .serviceCharge(0)
                .discount(0)
                .build();
    }

    @Test
    public void OrdersService_CreateOrder_ReturnsOrder() throws OrdersException{

        when(ordersRepository.save(Mockito.any(Orders.class))).thenReturn(mockOrderResponse);

        Orders createdOrder = ordersServiceImpl.createOrder(createOrder);

        Assertions.assertThat(createdOrder).isNotNull();
    }

    @Test
    public void OrdersService_GetOrders_ReturnPageOfOrders() throws OrdersException {
        Page<Orders> orders = Mockito.mock(Page.class);

        when(ordersRepository.findAll(Mockito.any(Pageable.class))).thenReturn(orders);

        OrdersResponseDTO receivedOrders = ordersServiceImpl.getOrders(0,10);

        Assertions.assertThat(receivedOrders).isNotNull();
    }

    @Test
    public void OrdersService_GetOrders_NoContent()throws OrdersException{
        Page<Orders> mockPage = new PageImpl<>(Collections.emptyList());
        OrdersException mockException = new OrdersException(HttpStatus.NO_CONTENT, "No Orders have been made yet");

        when(ordersRepository.findAll(Mockito.any(Pageable.class))).thenReturn(mockPage);

        OrdersException exception = assertThrows(OrdersException.class, () -> {
            ordersServiceImpl.getOrders(0,10);
        });
        assertEquals(mockException.status,exception.status);
        assertEquals(mockException.message, exception.message);
    }

    @Test
    public void OrdersService_GetOrders_DBExceptions()throws OrdersException{
        when(ordersRepository.findAll(Mockito.any(Pageable.class))).thenThrow(new DataAccessException("Database Connection Error") {});
        OrdersException exception = assertThrows(OrdersException.class, () -> {
            ordersServiceImpl.getOrders(0,10);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.status);
        assertEquals("Database Connection Error", exception.message);
    }

    @Test
    public void OrdersService_CreateOrder_DBExceptions()throws OrdersException{
        when(ordersRepository.save(Mockito.any(Orders.class))).thenThrow(new DataAccessException("Database Connection Error") {});
        OrdersException exception = assertThrows(OrdersException.class, () -> {
            ordersServiceImpl.createOrder(createOrder);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.status);
        assertEquals("Database Connection Error", exception.message);
    }
}
