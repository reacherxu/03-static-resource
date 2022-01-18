package com.richard.demo.utils;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.gson.Gson;
import com.richard.demo.utils.pojos.SubClass1;
import com.richard.demo.utils.pojos.TestClass1;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * refer to https://zhuanlan.zhihu.com/p/174786047
 * 
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/1/14 1:22 PM richard.xu Exp $
 */
@Slf4j
public class SpELTest {

    // 输出一个简单的字符串“Hello，编程帮”。
    @Test
    public void testStr() {
        // 构造解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 解析器解析字符串表达式
        Expression exp = parser.parseExpression("'Hello，编程帮'");
        // 获取表达式的值
        String message = (String) exp.getValue();
        System.out.println(message);
    }

    // SpEL 还可以调用方法、访问属性和调用构造函数。
    @Test
    public void testConcat() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Welcome，编程帮'.concat('！')");
        String message = (String) exp.getValue();
        System.out.println(message);
    }

    // 使用 SpEL 调用 String 的属性 bytes，将字符串转换为字节数组
    @Test
    public void testCallProperty() {
        ExpressionParser parser = new SpelExpressionParser();
        System.out.println("Hello 编程帮".getBytes());
        Expression exp = parser.parseExpression("'Hello 编程帮'.bytes");
        byte[] bytes = (byte[]) exp.getValue();
        for (int i = 0; i < bytes.length; i++) {
            System.out.print(bytes[i] + " ");
        }
    }

    // 测试变量
    @Test
    public void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        System.out.println(expression.getValue(context));
    }

    @Test
    public void testVariableExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("name", "路人甲java");
        context.setVariable("lesson", "Spring系列");

        // 获取name变量，lesson变量
        String name = parser.parseExpression("#name").getValue(context, String.class);
        System.out.println(name);
        String lesson = parser.parseExpression("#lesson").getValue(context, String.class);
        System.out.println(lesson);

        // StandardEvaluationContext构造器传入root对象，可以通过#root来访问root对象
        context = new StandardEvaluationContext("我是root对象");
        String rootObj = parser.parseExpression("#root").getValue(context, String.class);
        System.out.println(rootObj);

        // #this用来访问当前上线文中的对象
        String thisObj = parser.parseExpression("#this").getValue(context, String.class);
        System.out.println(thisObj);
    }

    // SpEL 还支持使用嵌套属性，下面将字符串转换为字节后获取长度
    @Test
    public void testNestProperty() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression exp = parser.parseExpression("'Hello 编程帮'.bytes.length");
        int length = (Integer) exp.getValue();
        System.out.println(length);


        TestClass1 request = TestClass1.builder().name("tc").married(false).age(10).build();
    }

    // 表达式赋值
    @Test
    public void testObject1() {
        TestClass1 request = TestClass1.builder().name("tc").married(false).age(10)
                .sub(SubClass1.builder().subPropertyA("a").subPropertyB(2).build()).build();
        log.info("before change, request is {}", JacksonUtil.writeStr(request));
        // request为root对象
        StandardEvaluationContext context = new StandardEvaluationContext(request);
        ExpressionParser parser = new SpelExpressionParser();
        parser.parseExpression("#root.name").setValue(context, "abc");
        parser.parseExpression("#root.age").setValue(context, "20");
        parser.parseExpression("#root.married").setValue(context, "true");
        parser.parseExpression("#root.sub.subPropertyA").setValue(context, "subA");
        log.info("after change, request is {}", JacksonUtil.writeStr(request));
    }

    @Test
    public void testObject2() {

        TestClass1 request = TestClass1.builder().name("tc").married(false).age(10).build();
        log.info("before change, request is {}", JacksonUtil.writeStr(request));
        // request
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("request", request);
        parser.parseExpression("#request.name").setValue(context, "路人甲java");
        log.info("after change, request is {}", JacksonUtil.writeStr(request));

    }

    /**
     * 不使用# 打头的spel 方式
     */
    @Test
    public void testMap2() {
        String el = "student.address.city";

        Map<String, Object> address = new HashMap<>();
        address.put("city", "北京");
        Map<String, Object> student = new HashMap<>();
        student.put("address", address);
        Map<String, Object> request = new LinkedHashMap<>();
        request.put("student", student);


        Expression expression = getExpression(el);

        Gson gson = new Gson();
        String strRequest = gson.toJson(request);
        log.info(strRequest);
        Map<String, Object> realReq = gson.fromJson(strRequest, Map.class);
        StandardEvaluationContext context = new StandardEvaluationContext(realReq);
        // 这里很关键，如果没有配置MapAccessor，那么只能用['c']['a']这种解析方式
        context.addPropertyAccessor(new MapAccessor());

        Object value = expression.getValue(context);
        System.out.println(value);
    }


    private Expression getExpression(String el) {
        ConcurrentHashMap<String, Expression> expressionMap = new ConcurrentHashMap<>(256);
        SpelExpressionParser parser = new SpelExpressionParser();
        Expression expression = expressionMap.get(el);
        if (expression != null) {
            return expression;
        }
        return expressionMap.computeIfAbsent(el, k -> parser.parseRaw(el));
    }

    /**
     * 标准的方式获取map值
     */
    @Test
    public void testMap() {
        ExpressionParser responseParser = new SpelExpressionParser();

        // 修改map元素值
        Map<String, Object> map = new HashMap<>();
        map.put("testVal1", 1);
        map.put("testVal2", "stringValue");
        map.put("testVal3", true);


        Gson gson = new Gson();
        String strResponse = gson.toJson(map);
        Map<String, Object> realResponse = gson.fromJson(strResponse, Map.class);

        EvaluationContext responseContext = new StandardEvaluationContext();
        responseContext.setVariable("response", realResponse);
        int testVal1 = responseParser.parseExpression("#response['testVal1']").getValue(responseContext, int.class);
        String testVal2 = responseParser.parseExpression("#response['testVal2']").getValue(responseContext, String.class);
        Boolean testVal3 = responseParser.parseExpression("#response['testVal3']").getValue(responseContext, Boolean.class);
        log.info("testVal1 {}, testVal2 {}, testVal3 {}",testVal1,testVal2,testVal3);

    }

    @Data
    public static class Car {
        private String name;
        private int age;

    }

    public static class User {
        private Car car;

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }

        @Override
        public String toString() {
            return "User{" + "car=" + car + '}';
        }
    }

    @Test
    public void test5() {
        User user = new User();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("user", user);

        ExpressionParser parser = new SpelExpressionParser();
        // 使用.符号，访问user.car.name会报错，原因：user.car为空
        try {
            System.out.println(parser.parseExpression("#user.car.name").getValue(context, String.class));
        } catch (EvaluationException | ParseException e) {
            System.out.println("出错了：" + e.getMessage());
        }
        // 使用安全访问符号?.，可以规避null错误
        System.out.println(parser.parseExpression("#user?.car?.name").getValue(context, String.class));

        Car car = new Car();
        car.setName("保时捷");
        user.setCar(car);

        System.out.println(parser.parseExpression("#user?.car?.toString()").getValue(context, String.class));
    }

}
