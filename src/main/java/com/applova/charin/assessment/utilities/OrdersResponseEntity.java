package com.applova.charin.assessment.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class OrdersResponseEntity {

    /**
     * What the response will be like for the Order API call, if there is an error
     * @param message The error message
     * @param status The status for the response must have
     * @return A response entity with the message and status code
     */
    public static ResponseEntity<Object> error(String message, HttpStatus status){
        Map<String, Object> responseEntity = new HashMap<>();
        responseEntity.put("message", message);

        return new ResponseEntity<>(responseEntity,status);
    }

    /**
     * What the response will be for an Order API call
     * @param orderList The return data list
     * @param status The status for the response must have
     * @return A response entity with the message and status code
     */
    public static ResponseEntity<Object> response(Object orderList, HttpStatus status){
        return new ResponseEntity<>(orderList,status);
    }
}
