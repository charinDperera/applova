package com.applova.charin.assessment2.dto;

import java.util.HashMap;
import java.util.Map;

public class ErrorDTO {

    private String message;
    private int code;
    private Throwable exception;
    private Map<String, String> errorMap;

    public ErrorDTO(String message) {
        this.message = message;
    }

    public ErrorDTO(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public ErrorDTO(String message, Throwable exception) {
        this.message = message;
        this.exception = exception;
    }

    public ErrorDTO(int code, Map<String, String> errorMap) {
        this.code = code;
        this.errorMap = errorMap;
    }

    public Throwable getException() {
        return exception;
    }

    public Map<String, String> getErrorMap() {
        return errorMap;
    }

    public String getMessage() {
        return message;
    }

    public int getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "message='" + message + '\'' +
                ", code=" + code +
                ", exception=" + exception +
                ", errorMap=" + errorMap +
                '}';
    }
}
