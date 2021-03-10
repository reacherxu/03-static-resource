/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * use ObjectMapper or Fastjson
 * refer to https://juejin.im/post/5d9733a26fb9a04e38584387
 * https://www.history-of-my-life.com/archives/547
 * 
 * @author richard.xu03@sap.com
 * @version $Id: JsonUtil.java, v 0.1 Mar 12, 2020 2:34:47 PM richard.xu Exp $
 */
@Slf4j
public class JsonUtil {

    /**
     * 不使用范型
     * 
     */
    @SuppressWarnings("rawtypes")
    @Test
    public void testRead() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String stringArray = "[\"color\" , \"Black\", \"type\" , \"BMW\" ]";
        String intArray = "[1,2,3]";
        String booleanArray = "[true,false,true]";

        List aList = objectMapper.readValue(stringArray, java.util.List.class);
        System.out.println(objectMapper.writeValueAsString(aList));
        List bList = objectMapper.readValue(intArray, java.util.List.class);
        System.out.println(objectMapper.writeValueAsString(bList));
        List cList = objectMapper.readValue(booleanArray, java.util.List.class);
        System.out.println(objectMapper.writeValueAsString(cList));
        objectMapper.readTree(stringArray);

    }

    /**
     * 使用范型
     * 
     */
    @Test
    public void testReadWithT() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String stringArray = "[\"color\" , \"Black\", \"type\" , \"BMW\" ]";
        String intArray = "[1,2,3]";
        String booleanArray = "[true,false,true]";

        List<String> aList = objectMapper.readValue(stringArray, new TypeReference<List<String>>() {});
        System.out.println(objectMapper.writeValueAsString(aList));
        List<Object> bList = objectMapper.readValue(intArray, new TypeReference<List<Object>>() {});
        System.out.println(bList);
        List<Object> cList = objectMapper.readValue(booleanArray, new TypeReference<List<Object>>() {});
        System.out.println(cList);
    }

    /**
     * JSON反序列化为Java对象
     * 
     */
    @Test
    public void testReadJavaClass() throws IOException {
        // object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Car car = objectMapper.readValue(json, Car.class);
        log.info("object mapper result is {}", car.toString());

        // fastjson
        Car car2 = JSON.parseObject(json, Car.class);
        log.info("fastjson result is {}", car2.toString());
        log.info("fastjson result, to JSON is {}", JSON.toJSONString(car));

    }

    /**
     * JSON反序列化为JsonNode对象
     * 
     */
    @Test
    public void testReadJsonNode() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{ \"color\" : \"Black\", \"type\" : \"FIAT\",\"quatity\" : 1  }";
        JsonNode jsonNode = objectMapper.readTree(json);
        String color = jsonNode.get("color").asText();
        System.err.println(color);
        int quatity = jsonNode.get("quatity").asInt();
        System.err.println(quatity);

        // jsonNode的fieldNames方法是获取jsonNode的所有的key值以及对应的value值
        Iterator<String> keys = jsonNode.fieldNames();
        while (keys.hasNext()) {
            String key = keys.next();
            System.out.println("key键是:" + key);
            JsonNode value = jsonNode.findValue(key);
            System.out.println("value键是:" + value);
        }
    }

    @Test
    public void testReadJsonArray() throws IOException {
        // 1. object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        String arrayJson =
                "[{\"number\":64,\"result\":\"SUCCESS\"},{\"number\":65,\"result\":\"FAILURE\"},{\"number\":66,\"result\":\"ABORTED\"},{\"number\":67,\"result\":\"SUCCESS\"}]";
        JsonNode jsonNode = objectMapper.readTree(arrayJson);
        // 如果是一个JsonNode数组，使用jsonNode.elements();读取数组中每个node
        Iterator<JsonNode> elements = jsonNode.elements();
        log.info("object mapper parsing array");
        while (elements.hasNext()) {
            JsonNode node = elements.next();
            log.info(node.toString());
        }

        // 2. fastjson
        // 2.1 know the type
        List<Object1> list = JSON.parseArray(arrayJson, Object1.class);
        log.info(list.toString());
        // 2.2 do not know type
        JSONArray array = JSON.parseArray(arrayJson);
        Iterator<Object> iterator = array.iterator();
        while (iterator.hasNext()) {
            JSONObject obj = (JSONObject) iterator.next();
            log.info("number is {}, result is {}", obj.getString("number"), obj.getString("result"));
        }

    }



}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Car {

    private String color;
    private String type;
}


@Data
@AllArgsConstructor
@NoArgsConstructor
class Object1 {
    private String number;
    private String result;
}
