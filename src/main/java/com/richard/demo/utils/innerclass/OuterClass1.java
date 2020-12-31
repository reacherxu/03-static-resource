package com.richard.demo.utils.innerclass;

import java.io.Serializable;

/**
 * 内部类的
 * 
 * @author richard.xu03@sap.com
 * @version $Id: OuterClass1.java, v 0.1 Oct 15, 2020 3:55:21 PM richard.xu Exp $
 */
public class OuterClass1 implements Serializable {
    private static final long serialVersionUID = 3372145258072057247L;
    private String str;

    public void outerDisplay() {
        System.out.println("outerClass...");
    }

    /**
     * 第一：成员内部类中不能存在任何static的变量和方法；
     * 第试用二：成员内部类是依附于外围类的，所以只有先创建了外围类才能够创建内部类。
     */
    public class InnerClass {
        public void innerDisplay() {
            // 使用外围内的属性
            str = "chenssy...";
            System.out.println(str);
            // 使用外围内的方法
            outerDisplay();
        }
    }

    /* 推荐使用getxxx()来获取成员内部类，尤其是该内部类的构造函数无参数时 */
    public InnerClass getInnerClass() {
        return new InnerClass();
    }

    public static void main(String[] args) {
        OuterClass1 outer = new OuterClass1();
        OuterClass1.InnerClass inner = outer.getInnerClass();
        inner.innerDisplay();
    }
}
