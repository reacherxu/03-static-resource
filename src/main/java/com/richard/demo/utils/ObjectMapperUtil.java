/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * refer to https://juejin.im/post/5d9733a26fb9a04e38584387
 * https://www.history-of-my-life.com/archives/547
 * 
 * @author richard.xu03@sap.com
 * @version $Id: ObjectMapperUtil.java, v 0.1 Mar 12, 2020 2:34:47 PM richard.xu Exp $
 */
public class ObjectMapperUtil {

    /**
     * 不使用范型
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
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

        // d
    }

    /**
     * 使用范型
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
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
     * @throws JsonMappingException
     * @throws JsonProcessingException
     */
    @Test
    public void testReadJavaClass() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "{\"color\":\"Black\",\"type\":\"BMW\"}";
        Car car = objectMapper.readValue(json, Car.class);
        System.out.println(car);
    }

    /**
     * JSON反序列化为JsonNode对象
     * 
     * @throws JsonMappingException
     * @throws JsonProcessingException
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
        ObjectMapper objectMapper = new ObjectMapper();
        String arrayJson =
                "[{\"number\":64,\"result\":\"SUCCESS\"},{\"number\":65,\"result\":\"FAILURE\"},{\"number\":66,\"result\":\"ABORTED\"},{\"number\":67,\"result\":\"SUCCESS\"}]";
        JsonNode jsonNode = objectMapper.readTree(arrayJson);
        // 如果是一个JsonNode数组，使用jsonNode.elements();读取数组中每个node
        Iterator<JsonNode> elements = jsonNode.elements();
        while (elements.hasNext()) {
            JsonNode node = elements.next();
            System.out.println(node.toString());
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
