package com.applova.charin.assessment.controller;

import com.applova.charin.assessment.dto.OrdersRequestDTO;
import com.applova.charin.assessment.service.OrdersService;
import com.applova.charin.assessment.utilities.OrdersException;
import com.applova.charin.assessment.utilities.OrdersResponseEntity;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "api/orders")
public class    OrdersController {
    @Autowired
    private OrdersService ordersService;

    @GetMapping
    public ResponseEntity<Object> getOrders(
            @Valid
            @Min(value = 0, message = "Page Number cannot be negative")
            @RequestParam(defaultValue = "0")
            int page,

            @Valid
            @Min(value = 0, message = "Page Size cannot be negative")
            @RequestParam(defaultValue = "10")
            int size)
    {
        try{
            return OrdersResponseEntity.response(ordersService.getOrders(page, size),HttpStatus.OK);
        } catch (OrdersException ex) {
            return OrdersResponseEntity.error(ex.message, ex.status);
        }
    }

    @PostMapping
    public ResponseEntity<Object> createOrder(@Valid @RequestBody OrdersRequestDTO orderRequest){
        try{
            return OrdersResponseEntity.response(ordersService.createOrder(orderRequest),HttpStatus.CREATED);
        } catch (OrdersException ex) {
            return OrdersResponseEntity.error(ex.message, ex.status);
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler({MethodArgumentNotValidException.class, ConstraintViolationException.class})
    public Map<String, String> handleValidations(MethodArgumentNotValidException ex) {
        Map<String, String> exceptions= new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String key = ((FieldError) error).getField();
            String message = error.getDefaultMessage();
            exceptions.put(key, message);
        });

        return exceptions;
    }
}
