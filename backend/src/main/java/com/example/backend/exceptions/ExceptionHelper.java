package com.example.backend.exceptions;

import jakarta.validation.ConstraintViolation;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.validation.BindingResult;

import java.util.Set;

public class ExceptionHelper {
    public static String formErrorMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .reduce((msg1, msg2) -> msg1 + "; " + msg2)
                .orElse("Validation failed");
    }
    public static <T> String formErrorMessage(Set<ConstraintViolation<T>> violations) {
        String message = "Validation failed: ";
        for (ConstraintViolation<T> violation : violations) {
            message += violation.getPropertyPath() + " " + violation.getMessage() + "\n";
        }
        return message;
    }
}
