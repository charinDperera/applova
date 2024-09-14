package com.applova.charin.assessment.utilities;

import lombok.AllArgsConstructor;
import lombok.ToString;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@ToString
public class OrdersException extends Throwable {
    public HttpStatus status;
    public String message;
}
