package com.richard.demo.services.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.richard.demo.services.Validator;

// 测试注入List
@Component
public class Validators {
    @Autowired
    List<Validator> validators;

    public void validate(String email, String password, String name) {
        for (Validator validator : this.validators) {
            validator.validate(email, password, name);
        }
    }
}
