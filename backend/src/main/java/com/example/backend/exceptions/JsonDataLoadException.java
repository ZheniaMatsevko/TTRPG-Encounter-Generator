package com.example.backend.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
public class JsonDataLoadException extends RuntimeException {
    public JsonDataLoadException(String message) {
        super(message);
    }
    public JsonDataLoadException(String message, Throwable cause) {
        super(message, cause);
    }
}
