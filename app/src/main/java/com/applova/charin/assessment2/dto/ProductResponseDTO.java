package com.applova.charin.assessment2.dto;

import com.applova.charin.assessment2.model.Product;

import java.util.List;

public class ProductResponseDTO {
    private List<Product> content;
    private String message;

    public ProductResponseDTO(List<Product> content, String message) {
        this.content = content;
        this.message = message;
    }

    public ProductResponseDTO(String message) {
        this.message = message;
    }

    public List<Product> getContent() {
        return content;
    }

    public void setContent(List<Product> content) {
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
        return "ProductResponseDTO{" +
                "content=" + content +
                ", message='" + message + '\'' +
                '}';
    }
}
