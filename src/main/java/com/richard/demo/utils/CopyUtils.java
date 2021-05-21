package com.richard.demo.utils;

import org.junit.Test;

import com.google.common.collect.Lists;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2021/5/12 2:26 PM richard.xu Exp $
 */
public class CopyUtils {

    @Test
    public void testCopy() {
        Customer customer =  Customer.builder().name("c1").property1("p1").build();
        customer.setOrders(Lists.newArrayList(new Order(1)));
        System.out.println(customer);

        // 这个也是浅复制
        Customer customer1 = customer.toBuilder().build();
        System.out.println(customer1);

    }
}
