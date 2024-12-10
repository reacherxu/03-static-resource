package com.richard.demo.services.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.richard.demo.services.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(1)
@Slf4j
public class EmailValidator implements Validator {
    public void validate(String email, String password, String name) {
        log.info("Start to validate email: {}", email);
        if (!email.matches("^[a-z0-9]+\\@[a-z0-9]+\\.[a-z]{2,10}$")) {
            log.error("invalid email: {}", email);
        }
    }
}
