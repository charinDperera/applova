package com.applova.charin.assessment2.dto;

public class ProductErrorDTO {
    private int position;
    private String key;
    private String message;

    public ProductErrorDTO(int position, String key, String message) {
        this.position = position;
        this.key = key;
        this.message = message;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "ProductErrorDTO{" +
                "position=" + position +
                ", key='" + key + '\'' +
                ", message='" + message + '\'' +
                '}';
    }
}
