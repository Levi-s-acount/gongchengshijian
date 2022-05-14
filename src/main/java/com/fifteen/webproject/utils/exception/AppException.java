package com.fifteen.webproject.utils.exception;


public class AppException extends RuntimeException{
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }
}
