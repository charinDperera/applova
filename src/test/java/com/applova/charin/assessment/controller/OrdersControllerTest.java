package com.applova.charin.assessment.controller;

import com.applova.charin.assessment.dto.OrdersRequestDTO;
import com.applova.charin.assessment.dto.OrdersResponseDTO;
import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.model.Product;
import com.applova.charin.assessment.service.OrdersService;
import com.applova.charin.assessment.utilities.OrdersException;
import com.applova.charin.assessment.utilities.OrdersResponseEntity;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;

@WebMvcTest(controllers = OrdersController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class OrdersControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private OrdersService ordersService;

    @InjectMocks
    private OrdersController ordersController;

    private Orders mockOrderResponse;
    private OrdersRequestDTO mockOrdersRequestDTO;
    private OrdersResponseDTO mockOrdersResponseDTO;

    @BeforeEach
    public void init(){
        mockOrdersRequestDTO = OrdersRequestDTO.builder()
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

        mockOrdersResponseDTO = OrdersResponseDTO.builder()
                .content(List.of(mockOrderResponse))
                .totalOrders(1L)
                .currentPage(0)
                .ordersPerPage(10)
                .totalPages(1)
                .build();
    }

    @Test
    public void OrdersController_CreateOrder_ReturnCreatedOrder() throws Exception, OrdersException {
        when(ordersService.createOrder(mockOrdersRequestDTO)).thenReturn(mockOrderResponse);
        ResponseEntity<Object> responseMock = OrdersResponseEntity.response(mockOrderResponse, HttpStatus.CREATED);

        ResponseEntity<Object> actualResponse = ordersController.createOrder(mockOrdersRequestDTO);

        assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
        assertEquals(responseMock.getBody(), actualResponse.getBody());
    }

    @Test
    public void OrdersController_CreateOrder_ThrowException() throws OrdersException {
        when(ordersService.createOrder(Mockito.any(OrdersRequestDTO.class)))
                .thenThrow(new OrdersException(HttpStatus.INTERNAL_SERVER_ERROR, "Connection Error"));

        ResponseEntity<Object> responseMock = OrdersResponseEntity.error("Connection Error", HttpStatus.INTERNAL_SERVER_ERROR);

        ResponseEntity<Object> actualResult = ordersController.createOrder(mockOrdersRequestDTO);

        assertEquals(actualResult.getStatusCode(), responseMock.getStatusCode());
        assertEquals(actualResult.getBody(), responseMock.getBody());
    }

    @Test
    public void OrdersController_GetOrders_ReturnOrdersPage() throws OrdersException, Exception {
        when(ordersService.getOrders(0, 10)).thenReturn(mockOrdersResponseDTO);

        ResultActions response = mockMvc.perform(get("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .param("page", "0")
                .param("size", "10"));

        response.andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.content.size()",
                        CoreMatchers.is(mockOrdersResponseDTO.getContent().size())));
    }

    @Test
    public void OrdersController_GetOrders_ThrowException() throws OrdersException {
        when(ordersService.getOrders(Mockito.any(Integer.class),Mockito.any(Integer.class)))
                .thenThrow(new OrdersException(HttpStatus.INTERNAL_SERVER_ERROR, "Connection Error"));

        ResponseEntity<Object> responseMock = OrdersResponseEntity.error("Connection Error", HttpStatus.INTERNAL_SERVER_ERROR);

        ResponseEntity<Object> actualResult = ordersController.getOrders(0,10);

        assertEquals(actualResult.getStatusCode(), responseMock.getStatusCode());
        assertEquals(actualResult.getBody(), responseMock.getBody());
    }

    @Test
    public void OrdersController_ExceptionHandler() throws Exception{
        OrdersRequestDTO errorDTO = OrdersRequestDTO.builder()
                .products(List.of(Product.builder().build()))
                .waiter("")
                .build();

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(errorDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.waiter").value("Waiter name cannot be empty"));
    }
}
