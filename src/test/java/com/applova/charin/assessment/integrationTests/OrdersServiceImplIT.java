package com.applova.charin.assessment.integrationTests;

import com.applova.charin.assessment.dto.OrdersRequestDTO;
import com.applova.charin.assessment.dto.OrdersResponseDTO;
import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.model.Product;
import com.applova.charin.assessment.service.OrdersService;
import com.applova.charin.assessment.utilities.OrdersException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class OrdersServiceImplIT {
    @Autowired
    private OrdersService ordersService;

    private OrdersRequestDTO createRequest;

    @BeforeEach
    public void init(){
        createRequest = OrdersRequestDTO.builder()
                .products(List.of(
                        Product.builder()
                                .productName("Rice")
                                .quantity(5)
                                .price(500.00)
                                .build()))
                .waiter("Kamal")
                .build();
    }

    @Test
    public void testCreatingOrders() throws OrdersException {
        final Orders orderResponse = ordersService.createOrder(createRequest);

        Assertions.assertEquals(1, orderResponse.getOrderNum());
    }

    @Test
    public void testGettingOrders() throws OrdersException {
        final OrdersResponseDTO responseDTO = ordersService.getOrders(0, 100);

        Assertions.assertEquals(100, responseDTO.getOrdersPerPage());
    }

}
