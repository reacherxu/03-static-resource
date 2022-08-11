package com.richard.demo.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
import org.junit.Test;

import lombok.extern.slf4j.Slf4j;

/**
 * refer to https://blog.csdn.net/wmq880204/article/details/120565163
 * 
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/8/10 17:02 richard.xu Exp $
 */
@Slf4j
public class FunctionUtil {

    /**
     * Function ,参数:T.R,返回值:R
     * 方法型：输入一个参数得到一个结果
     */
    @Test
    public void testApply() {
        Function<Integer, Integer> triangle = arg -> arg * 3;
        Function<Integer, Integer> square = arg -> arg * arg;
        int result1 = triangle.apply(2); // result1: 3*2=6
        int result2 = square.apply(2);// result2: 4
        log.info("result1 is {},result2 is {}", result1, result2);

        Function<Integer, String> intToString = integer -> {
            int temp = integer * integer;
            return "Hello " + temp;
        };
        log.info(intToString.apply(15));
    }

    /**
     * 复合函数可以理解成是基本Function接口的二元操作
     */
    @Test
    public void testCompose() {
        Function<Integer, Integer> triangle = arg -> arg * 3;
        Function<Integer, Integer> square = arg -> arg * arg;

        Function<Integer, Integer> area1 = triangle.compose(square);
        Function<Integer, Integer> area2 = square.compose(triangle);
        int result1 = area1.apply(2);// tri(squ(1))=4*3
        int result2 = area2.apply(2);// squ(tri(1))=6*6
        log.info("result1 is {},result2 is {}", result1, result2);
    }

    /**
     * 输入是什么。输出就是什么
     */
    @Test
    public void testdentity() {
        Function<Integer, Integer> id = Function.identity();
        int output = id.apply(10);
        Assert.assertEquals(10, output);
    }

    /**
     * 2个输入函数
     */
    @Test
    public void testBiFunction() {
        BiFunction<Integer,Double,Double> biFunction = (x, y) -> {
            ++x;
            ++y;
            return x+y;
        };
        System.out.println(biFunction.apply(1,2.3));
    }

    /**
     * Consumer ,参数:T, 返回值:无参
     * 消费型：传入一个指定参数，无返回值
     */
    @Test
    public void testConsumer() {
        // 设置好Consumer实现方法
        Consumer<Integer> square = x -> System.out.println("平方计算 : " + x * x);
        square.accept(2);

        List<User> userList = new ArrayList<>();
        userList.add(new User(1, "frank"));
        userList.add(new User(2, "node"));

        // JDK的iterator接口就是使用consumer来操作的
        userList.forEach(user -> user.setId(user.getId() + 1));
        log.info(JacksonUtil.writeStr(userList));
    }

    /**
     * variables for pre and exception
     * example 'element[0].propertyA.propertyB'
     */
    public static final String CDT_VAR = "\\[\\d+]";

    /**
     * 有相似的代码逻辑，但是有块动态实现，就可以传函数式方法
     *
     * @param source
     * @param regex
     * @param replace
     * @return
     */
    public String translateInput(String source, String regex, Function<String, String> replace) {
        if (StringUtils.isBlank(source)) {
            return source;
        }

        StringBuffer translation = new StringBuffer();
        Pattern variablePattern = Pattern.compile(regex);
        Matcher variableMatcher = variablePattern.matcher(source);
        while (variableMatcher.find()) {
            String var = variableMatcher.group();
            String replacement = replace.apply(var);
            variableMatcher.appendReplacement(translation, replacement);
        }
        variableMatcher.appendTail(translation);
        return translation.toString();
    }

    @Test
    public void testTranslate() {
        Function<String, String> removeBracket = x -> x.substring(1, x.length() - 1);
        String result = translateInput("array[883]", CDT_VAR, removeBracket);
        log.info("removeBracket {}",result);

        Function<String, String> toGet = x -> {
            x = x.replace("[", "(").replace("]", ")");
            return ".get" + x;
        };
        result = translateInput("array[883]", CDT_VAR, toGet);
        log.info("to get {}",result);
    }

    /**
     * Predicate 一个函数式接口，属于java.util.function包，主要用来对输入的对象按照指定的条件进行过滤。
     * T是你传进去的参数，可以是任意类型，返回的是一个布尔值。意思就是：你给我一个值，我告诉你对不对？
     */
    @Test
    public void testPredicate() {
        Predicate<User> olderThan10 = user -> user.getId() > 10;
        Predicate<User> isStudent = user -> StringUtils.equals("student",user.getName());

        User user1 = new User(3,"baby");
        User user2 = new User(13,"student");
        User user3 = new User(9,"student");

        System.out.println(olderThan10.test(user1));
        System.out.println(olderThan10.test(user2));
        System.out.println(isStudent.and(olderThan10).test(user2));
        System.out.println(isStudent.or(olderThan10).test(user3));
    }

    /**
     * 没有输入，产生一个数据
     */
    @Test
    public void testSupplier() {
        Supplier<String> supplier = () -> "我是一个提供者";
        System.out.println(supplier.get());
    }


}
