package com.example.marketflow.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalMvcExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleEmailAlreadyExists(
            EmailAlreadyExistsException exception,
            Model model
    ) {
        model.addAttribute("message", exception.getMessage());
        return "register-duplicate";
    }

    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String function2(InvalidCredentialsException message,Model model){
        model.addAttribute("message",message.getMessage());
        return "Page1";
    }
}
