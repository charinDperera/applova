package com.applova.charin.assessment2.dto;

import com.applova.charin.assessment2.model.Orders;

public class CreateOrdersResponseDTO {
    private Orders content;

    private String message;

    public CreateOrdersResponseDTO() {
    }

    public CreateOrdersResponseDTO(Orders content, String message) {
        this.content = content;
        this.message = message;
    }

    public Orders getContent() {
        return content;
    }

    public void setContent(Orders content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "CreateOrdersResponseDTO{" +
                "content=" + content +
                ", message='" + message + '\'' +
                '}';
    }
}
