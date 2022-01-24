package com.richard.demo.utils.reflection;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.ClassUtils;
import org.springframework.util.ReflectionUtils;

import com.richard.demo.DemoApplication;
import com.richard.demo.services.impl.OrderInfoDaoAImpl;

import lombok.extern.slf4j.Slf4j;

/**
 * 这是一个起spring boot 的test
 */
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
            // Class<?> cls = Class.forName("com.richard.demo.utils.reflection.TestPrint");
            Class<?> cls = TestPrint.class;
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
            // } catch (ClassNotFoundException e) {
            // e.printStackTrace();
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
        // Class clzss = Class.forName("com.richard.demo.utils.reflection.TestPrint");
        Class<?> clzss = TestPrint.class;

        Object obj = clzss.getDeclaredConstructor().newInstance();
        // 获取执行方法对象.形参是待执行方法名
        Method method = clzss.getDeclaredMethod("print", String.class, String.class);
        method.setAccessible(true);
        // 执行方法
        Object result = method.invoke(obj, "first", "second");
        log.info(result.toString());


        /// for next test case
        clzss = OrderInfoDaoAImpl.class;
        obj = clzss.getDeclaredConstructor().newInstance();
        // 获取执行方法对象.形参是待执行方法名
        method = clzss.getDeclaredMethod("queryOrderList");
        method.setAccessible(true);
        // 执行方法
        result = method.invoke(obj);
        log.info(result.toString());
    }

    @Test
    public void test() {

        try {
            // 调用非静态方法
            Class<?> order = ClassUtils.forName("com.richard.demo.services.impl.OrderInfoDaoAImpl", ClassUtils.getDefaultClassLoader());
            Method method = ReflectionUtils.findMethod(order, "queryOrderList");
            if (Objects.nonNull(method)) {
                Object obj = order.getDeclaredConstructor().newInstance();
                Object result = ReflectionUtils.invokeMethod(method, obj);
                log.info("order result is {}", result.toString());
            }

            // 调用静态方法
            Class<?> julBridge = ClassUtils.forName("org.slf4j.bridge.SLF4JBridgeHandler", ClassUtils.getDefaultClassLoader());
            Method removeHandlers = ReflectionUtils.findMethod(julBridge, "removeHandlersForRootLogger");
            if (removeHandlers != null) {
                log.info("Removing all previous handlers for JUL to SLF4J bridge");
                ReflectionUtils.invokeMethod(removeHandlers, null);
            }

            Method install = ReflectionUtils.findMethod(julBridge, "install");
            if (install != null) {
                log.info("Installing JUL to SLF4J bridge");
                ReflectionUtils.invokeMethod(install, null);
            }
        } catch (ClassNotFoundException | NoSuchMethodException ignored) {
            // Indicates the java.util.logging bridge is not in the classpath. This is not an indication of a
            // problem.
            log.info("JUL to SLF4J bridge is not available on the classpath");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

}
