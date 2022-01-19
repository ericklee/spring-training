package com.hkjc.accountsvc.controller;

import com.hkjc.accountsvc.dto.ErrorDto;
import com.hkjc.accountsvc.exception.AccountNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class AccountControllerAdvice {

    @ExceptionHandler(AccountNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorDto handleAccountNotFoundException() {
        return new ErrorDto();
    }
}
