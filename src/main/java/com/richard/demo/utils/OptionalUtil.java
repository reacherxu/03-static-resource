/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.util.Optional;
import org.junit.Test;
import lombok.Data;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: OptionalUtil.java, v 0.1 Jun 7, 2020 9:13:23 AM richard.xu Exp $
 */
public class OptionalUtil {

    /**
     * 用optional 解决NPE 问题
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
