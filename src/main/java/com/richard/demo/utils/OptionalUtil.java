/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: OptionalUtil.java, v 0.1 Jun 7, 2020 9:13:23 AM richard.xu Exp $
 */
@Slf4j
public class OptionalUtil {

    /**
     * 用optional.map 解决NPE 问题
     */
    @Test
    public void testNPE() {
        OptionalPerson person = new OptionalPerson();
        OptionalCar ocar = new OptionalCar();
        person.setCar(ocar);
        // non-compliant, not suggested
        String brand = Optional.ofNullable(person).map(p -> p.getCar()).map(car -> car.getWheel()).map(wheel -> wheel.getBrand())
                .orElse("no wheel");
        System.out.println(brand);

        // compliant solution, suggested
        String brandNew = Optional.ofNullable(person).map(OptionalPerson::getCar).map(OptionalCar::getWheel).map(OptionalWheel::getBrand)
                .orElse("no wheel");
        System.out.println(brandNew);

        OptionalWheel owheel = new OptionalWheel();
        owheel.setBrand("Nike");
        ocar.setWheel(owheel);
        brand = Optional.ofNullable(person).map(p -> p.getCar()).map(car -> car.getWheel()).map(wheel -> wheel.getBrand())
                .orElse("no wheel");
        System.out.println(brand);
    }

    /**
     * Optional 的正确使用方式
     */
    @Test
    public void testNullObject() {
        // 1. 存在才对它做点什么
        User king = new User(1, "king");
        Optional<User> userOpt = Optional.of(king);
        userOpt.ifPresent(user -> {
            System.out.println(user.getName());
        });
        // filter()方法接受参数为Predicate对象，用于对Optional对象进行过滤，如果符合Predicate的条件，返回Optional对象本身，否则返回一个空的Optional对象
        userOpt.filter(user -> user.getName().equals("king")).ifPresent(user -> {
            System.out.println(user.getName());
        });

        // 而不要下边这样
        if (userOpt.isPresent()) {
            System.out.println(userOpt.get());
        }
    }

    public User getUser(User user) {
        // 1.存在即返回, 无则提供默认值
        Optional<User> userOpt = Optional.ofNullable(user);
        // return userOpt.orElse(null);

        // 2.或者用这种方式，存在即返回, 无则由函数来产生
        return userOpt.orElseGet(() -> getAnotherUser("test"));
    }

    /**
     * Optional循环遍历集合
     */
    @Test
    public void testNullCollection() {
        List<String> list = null;
        Optional.ofNullable(list).orElse(new ArrayList<String>()).forEach(element -> {
            System.out.println(element);
        });
    }

    /**
     * test chain
     * @param name
     * @return
     */
    public User getAnotherUser(String name) {
        return new User(0, name);
    }

}


@Data
@AllArgsConstructor
@NoArgsConstructor
class User {
    private int id;
    private String name;
}

@Data
class OptionalWheel {
    private String brand;
}


@Data
class OptionalCar {
    private OptionalWheel wheel;
}


@Data
class OptionalPerson {
    private OptionalCar car;
}
