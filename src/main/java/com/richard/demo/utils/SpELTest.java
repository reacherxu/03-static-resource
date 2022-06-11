package com.richard.demo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.junit.Test;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.expression.BeanFactoryResolver;
import org.springframework.context.expression.MapAccessor;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.EvaluationException;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.ParseException;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import com.google.gson.Gson;
import com.richard.demo.services.impl.OrderInfoDaoAImpl;
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

    /**
     * SpEL支持的字面量包括：字符串、数字类型（int、long、float、double）、布尔类型、null类型。
     * 输出一个简单的字符串“Hello，编程帮”。
     */
    @Test
    public void testBasic() {
        // 构造解析器
        ExpressionParser parser = new SpelExpressionParser();
        // 解析器解析字符串表达式
        Expression exp = parser.parseExpression("'Hello，编程帮'");
        // 获取表达式的值
        String message = (String) exp.getValue();
        System.out.println("message is " + message);
        String str1 = parser.parseExpression("'Hello World!'").getValue(String.class);
        int int1 = parser.parseExpression("1").getValue(Integer.class);
        long long1 = parser.parseExpression("-1L").getValue(long.class);
        float float1 = parser.parseExpression("1.1").getValue(Float.class);
        double double1 = parser.parseExpression("1.1E+2").getValue(double.class);
        int hex1 = parser.parseExpression("0xa").getValue(Integer.class);
        long hex2 = parser.parseExpression("0xaL").getValue(long.class);
        boolean true1 = parser.parseExpression("true").getValue(boolean.class);
        boolean false1 = parser.parseExpression("false").getValue(boolean.class);
        Object null1 = parser.parseExpression("null").getValue(Object.class);

        System.out.println("str1=" + str1);
        System.out.println("int1=" + int1);
        System.out.println("long1=" + long1);
        System.out.println("float1=" + float1);
        System.out.println("double1=" + double1);
        System.out.println("hex1=" + hex1);
        System.out.println("hex2=" + hex2);
        System.out.println("true1=" + true1);
        System.out.println("false1=" + false1);
        System.out.println("null1=" + null1);
    }

    /**
     * 类类型表达式
     * 使用“T(Type)”来表示java.lang.Class实例，“Type”必须是类全限定名，“java.lang”包除外，即该包下的类可以不指定包名；
     * 使用类类型表达式还可以进行访问类静态方法及类静态字段。
     */
    @Test
    public void testClassTypeExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        // java.lang包类访问
        Class<String> result1 = parser.parseExpression("T(String)").getValue(Class.class);
        System.out.println(result1);

        // 其他包类访问
        String expression2 = "T(com.richard.demo.utils.JacksonUtil)";
        Class<JacksonUtil> value = parser.parseExpression(expression2).getValue(Class.class);
        System.out.println(value == JacksonUtil.class);

        // 类静态字段访问
        int result3 = parser.parseExpression("T(Integer).MAX_VALUE").getValue(int.class);
        System.out.println(result3 == Integer.MAX_VALUE);

        // 类静态方法调用
        int result4 = parser.parseExpression("T(Integer).parseInt('1')").getValue(int.class);
        System.out.println(result4);
        // 可以进行访问类静态方法
        EvaluationContext context = new StandardEvaluationContext();
        TestClass1 request = TestClass1.builder().name("tc").married(false).age(10)
                .sub(SubClass1.builder().subPropertyA("a").subPropertyB(2).build()).build();
        context.setVariable("obj", request);
        String expression2_1 = "T(com.richard.demo.utils.JacksonUtil).writeStr(#obj)";
        String result = parser.parseExpression(expression2_1).getValue(context, String.class);
        System.out.println(result);
    }

    /**
     * 类实例化
     * 类实例化同样使用java关键字“new”，类名必须是全限定名，但java.lang包内的类型除外，如String、Integer。
     * SpEL支持instanceof运算符，跟Java内使用同义
     */
    @Test
    public void testConstructorExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        String result1 = parser.parseExpression("new String('路人甲java')").getValue(String.class);
        System.out.println(result1);

        Date result2 = parser.parseExpression("new java.util.Date()").getValue(Date.class);
        System.out.println(result2);

        // 实例化对象，无需使用反射
        OrderInfoDaoAImpl order =
                parser.parseExpression("new com.richard.demo.services.impl.OrderInfoDaoAImpl()").getValue(OrderInfoDaoAImpl.class);
        String result = order.queryOrderList();
        log.info("order result is {}", result);

        ExpressionParser newParser = new SpelExpressionParser();
        Boolean value = newParser.parseExpression("'路人甲' instanceof T(String)").getValue(Boolean.class);
        System.out.println(value);

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

    /**
     * 测试变量
     * 1）创建解析器：SpEL使用ExpressionParser接口表示解析器，提供SpelExpressionParser默认实现；
     *
     * 2）解析表达式：使用ExpressionParser的parseExpression来解析相应的表达式为Expression对象。
     *
     * 3）构造上下文：准备比如变量定义等等表达式需要的上下文数据。
     *
     * 4）求值：通过Expression接口的getValue方法根据上下文获得表达式值。
     */
    @Test
    public void test1() {
        ExpressionParser parser = new SpelExpressionParser();
        Expression expression = parser.parseExpression("('Hello' + ' World').concat(#end)");
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("end", "!");
        System.out.println(expression.getValue(context));
    }

    /**
     * 变量定义通过EvaluationContext接口的setVariable(variableName, value)方法定义；
     * 在表达式中使用"#variableName"引用；
     * 除了引用自定义变量，SpE还允许引用根对象及当前上下文对象，使用"#root"引用根对象，使用"#this"引用当前上下文对象；
     */
    @Test
    public void testVariableExpression() {
        ExpressionParser parser = new SpelExpressionParser();
        EvaluationContext context = new StandardEvaluationContext();
        context.setVariable("name", "路人甲java");
        context.setVariable("lesson", "Spring系列");

        // 获取name变量，lesson变量
        String name = parser.parseExpression("#name").getValue(context, String.class);
        System.out.println(name);
        parser.parseExpression("#name").setValue(context, "路人甲python");
        String name1 = parser.parseExpression("#name").getValue(context, String.class);
        System.out.println("after changing primitive type, name is " + name1);

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
     * refer to https://www.jianshu.com/p/b53965977d26
     * 为了更优雅的兼容map解析，需要对原有的StandardEvaluationContext添加一个属性访问器：MapAccessor
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
     * sample : https://blog.csdn.net/sinat_34863938/article/details/115915357
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

    // 解析json 对象
    @Test
    public void testJsonObject() throws IOException {
        ExpressionParser responseParser = new SpelExpressionParser();
        Gson gson = new Gson();
        String json =
                "{\"text\":\"张三\",\"expensive\":6,\"body\":{\"rvNoNum\":23,\"rvNoRecords\":[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":null}]}}";

        Map<String, Object> realResponse = gson.fromJson(json, Map.class);
        // ObjectMapper objectMapper = new ObjectMapper();
        // JsonNode realResponse = objectMapper.readTree(json);

        EvaluationContext responseContext = new StandardEvaluationContext();
        responseContext.setVariable("response", realResponse);
        // 简单属性
        String testVal1 = responseParser.parseExpression("#response['text']").getValue(responseContext, String.class);
        // 对象属性
        String testVal2 = responseParser.parseExpression("#response['body']['rvNoNum']").getValue(responseContext, String.class);
        // 数组属性
        String testVal3 =
                responseParser.parseExpression("#response['body']['rvNoRecords'][1]['score']").getValue(responseContext, String.class);
        log.info("testVal1 {}, testVal2 {}, testVal3 {}", testVal1, testVal2, testVal3);
    }

    // 解析数组对象
    @Test
    public void testJsonArray() throws IOException {
        ExpressionParser responseParser = new SpelExpressionParser();
        Gson gson = new Gson();
        String json =
                "[{\"text\":\"张三\",\"expensive\":6,\"body\":{\"rvNoNum\":23,\"rvNoRecords\":[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":null}]}}]";

        Collection realResponse = gson.fromJson(json, Collection.class);
        EvaluationContext responseContext = new StandardEvaluationContext();
        responseContext.setVariable("response", realResponse);
        Object testVal1 = responseParser.parseExpression("#response[0]['text']").getValue(responseContext);

        Object testVal2 = responseParser.parseExpression("#response[0]['body']['rvNoNum']").getValue(responseContext);
        Object testVal3 = responseParser.parseExpression("#response[0]['body']['rvNoRecords'][1]['score']").getValue(responseContext);
        log.info("testVal1 {} : type {}, testVal2 {} : type {}, testVal3 {} : type {}", testVal1, testVal1 instanceof String, testVal2,
                testVal2 instanceof Double, testVal3, testVal3 instanceof Double);
    }

    /**
     * 列表，字典，数组元素修改
     * 可以使用赋值表达式或Expression接口的setValue方法修改；
     */
    @Test
    public void testCollection() {
        ExpressionParser parser = new SpelExpressionParser();

        // 修改list元素值
        List<Integer> list = new ArrayList<Integer>();
        list.add(1);
        list.add(2);

        EvaluationContext context1 = new StandardEvaluationContext();
        context1.setVariable("collection", list);
        parser.parseExpression("#collection[1]").setValue(context1, 4);
        int result1 = parser.parseExpression("#collection[1]").getValue(context1, int.class);
        System.out.println(result1);
        System.out.println(list);

        // 修改map元素值
        Map<String, Integer> map = new HashMap<String, Integer>();
        map.put("a", 1);
        EvaluationContext context2 = new StandardEvaluationContext();
        context2.setVariable("map", map);
        parser.parseExpression("#map['a']").setValue(context2, 4);
        Integer result2 = parser.parseExpression("#map['a']").getValue(context2, int.class);
        System.out.println(result2);
        System.out.println(map);
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

    /**
     * 对象属性存取及安全导航表达式
     */
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

    /**
     * SpEL支持使用“@”符号来引用Bean，在引用Bean时需要使用BeanResolver接口实现来查找Bean，
     * Spring提供BeanFactoryResolver实现。
     */
    @Test
    public void test6() {
        DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
        User user = new User();
        Car car = new Car();
        car.setName("保时捷");
        user.setCar(car);
        factory.registerSingleton("user", user);

        StandardEvaluationContext context = new StandardEvaluationContext();
        context.setBeanResolver(new BeanFactoryResolver(factory));

        ExpressionParser parser = new SpelExpressionParser();
        User userBean = parser.parseExpression("@user").getValue(context, User.class);
        System.out.println(userBean);
        System.out.println(userBean == factory.getBean("user"));
    }

}
