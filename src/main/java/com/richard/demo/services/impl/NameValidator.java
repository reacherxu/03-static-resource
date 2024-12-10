package com.richard.demo.services.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.richard.demo.services.Validator;

import lombok.extern.slf4j.Slf4j;

@Component
@Order(2)
@Slf4j
public class NameValidator implements Validator {


    public void validate(String email, String password, String name) {
        log.info("Start to validate name: {}", name);
        if (name == null || StringUtils.isBlank(name) || name.length() > 20) {
            log.error("invalid name: {}", name);
        }
    }
}
