package com.richard.demo.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;

import com.richard.demo.DemoApplication;

import lombok.extern.slf4j.Slf4j;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {DemoApplication.class})
@Slf4j
public class ReflectionUtil {

    @Autowired
    private ApplicationContext applicationContext;

    @Test
    public void testSpringBean() {
        try {
            // 获取class对象
            Class<?> cls = Class.forName("com.richard.demo.utils.reflection.TestPrint");
            // 获取spring中的bean对象
            Object bean = applicationContext.getBean(cls);

            // 缺少setAccessible(true)方法，使用继承父类（AccessibleObject类）来的setAccessible()方法，
            // 来设置或取消访问检查，以达到访问私有对象的目的
            // Method method=clazz.getDeclaredMethod(name);//可以调用类中的所有方法（不包括父类中继承的方法）
            // Method method=clazz.getMethod(name);//可以调用类中有访问权限的方法（包括父类中继承的方法）
            Method method = cls.getDeclaredMethod("print", String.class, String.class);
            method.setAccessible(true);
            // 执行方法
            String result = (String) method.invoke(bean, "first", "second");
            log.info(result);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testJava() throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException,
            InstantiationException {
        Class clzss = Class.forName("com.richard.demo.utils.reflection.TestPrint");
        Object obj = clzss.getDeclaredConstructor().newInstance();
        // 获取执行方法对象.形参是待执行方法名
        Method method = clzss.getDeclaredMethod("print", String.class, String.class);
        method.setAccessible(true);
        // 执行方法
        Object result = method.invoke(obj, "first", "second");
        log.info(result.toString());
    }

}
