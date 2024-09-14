package com.applova.charin.assessment.utilities;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class OrdersResponseEntityTest {

    @Test
    public void OrderResponseEntity_Error_ReturnErrorResponse(){
        String message="An error occurred somewhere";
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ResponseEntity<Object> responseEntity = OrdersResponseEntity.error(message, status);

        Assertions.assertThat(responseEntity).isNotNull();
    }

    @Test
    public void OrderResponseEntity_Response_ReturnDataResponse(){
        Object orderList = Mockito.mock(Page.class);
        HttpStatus status = HttpStatus.BAD_REQUEST;

        ResponseEntity<Object> responseEntity = OrdersResponseEntity.response(orderList, status);

        Assertions.assertThat(responseEntity).isNotNull();
    }
}
