package com.richard.demo;

import lombok.Data;

@Data
public class Person {
    public String name;
    public int age;
    public String address;

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public Person(String name, int age, String address) {
        this.name = name;
        this.age = age;
        this.address = address;
    }


    @Override
    public String toString() {
        return name + ":" + age + ":" + address;
    }
}
