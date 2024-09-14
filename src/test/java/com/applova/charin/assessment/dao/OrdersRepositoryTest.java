package com.applova.charin.assessment.dao;

import com.applova.charin.assessment.model.Orders;
import com.applova.charin.assessment.model.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class OrdersRepositoryTest {
    @Autowired
    private OrdersRepository ordersRepository;

    private Orders createOrder;

    @BeforeEach
    public void init(){
        createOrder = Orders.builder()
                .products(List.of(
                                Product.builder()
                                        .productName("Rice")
                                        .quantity(5)
                                        .price(100.00)
                                        .build(),
                                Product.builder()
                                        .productName("Sprite")
                                        .quantity(2)
                                        .price(60.00)
                                        .build()
                        )
                )
                .waiter("Saman")
                .build();
    }

    @Test
    public void OrdersRepository_Save_ReturnsSavedOrder(){
        Orders savedOrder = ordersRepository.save(createOrder);

        Assertions.assertThat(savedOrder).isNotNull();
        Assertions.assertThat(savedOrder.getOrderNum()).isGreaterThan(0);
    }

    @Test
    public void OrdersRepository_FindAll_ReturnsPageOfOrders(){
        ordersRepository.save(createOrder);
        Page<Orders> pageList = ordersRepository.findAll(PageRequest.of(0, 10));

        Assertions.assertThat(pageList).isNotNull();
        Assertions.assertThat(pageList.isEmpty()).isFalse();
        Assertions.assertThat(pageList.getTotalElements()).isEqualTo(1);
    }
}
