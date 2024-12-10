package com.richard.demo.services.impl;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.richard.demo.services.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(3)
@Slf4j
public class PasswordValidator implements Validator {
    public void validate(String email, String password, String name) {
        log.info("Start to validate password: {}", password);

        if (!password.matches("^.{6,20}$")) {
            log.error("invalid password: {}", password);
        }
    }
}
