/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils.innerclass;

import java.io.Serializable;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: OuterClass2.java, v 0.1 Oct 15, 2020 4:07:38 PM richard.xu Exp $
 */
public class OuterClass2 implements Serializable {
    private static final long serialVersionUID = 6653463866995540628L;
    private String sex = "male";
    public static String name = "chenssy";

    /**
     * 静态内部类
     * 1、 它的创建是不需要依赖于外围类的。
     * 2、 它不能使用任何外围类的非static成员变量和方法。
     */
    static class InnerClass1 {
        /* 在静态内部类中可以存在静态成员 */
        public static String _name1 = "chenssy_static";

        public void display() {
            /*
             * 静态内部类只能访问外围类的静态成员变量和方法
             * 不能访问外围类的非静态成员变量和方法
             */
            System.out.printf("OutClass name : %s%n", name);
        }
    }

    /**
     * 非静态内部类
     */
    class InnerClass2 {
        /* 非静态内部类中不能存在静态成员 */
        public String _name2 = "chenssy_inner";

        /* 非静态内部类中可以调用外围类的任何成员,不管是静态的还是非静态的 */
        public void display() {
            System.out.println(String.format("OuterClass name：%s, sex: %s", name, sex));
        }
    }

    /**
     * @desc 外围类方法
     * @author chenssy
     * @data 2013-10-25
     * @return void
     */
    public void display() {
        /* 外围类访问静态内部类：内部类. */
        System.out.println(InnerClass1._name1);
        /* 静态内部类 可以直接创建实例不需要依赖于外围类 */
        new InnerClass1().display();

        /* 非静态内部的创建需要依赖于外围类 */
        OuterClass2.InnerClass2 inner2 = new OuterClass2().new InnerClass2();
        /* 方位非静态内部类的成员需要使用非静态内部类的实例 */
        System.out.println(inner2._name2);
        inner2.display();
    }

    public static void main(String[] args) {
        OuterClass2 outer = new OuterClass2();
        outer.display();
    }
}