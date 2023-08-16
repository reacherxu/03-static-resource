/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.io.IOException;
import java.util.*;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.*;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
     * 使用alibaba fastjson
     * 将JSON字符串反序列化为Java对象
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

        // 2. fastjson
        // 2.1 know the type
        String arrayJson =
                "[{\"number\":64,\"result\":\"SUCCESS\"},{\"number\":65,\"result\":\"FAILURE\"},{\"number\":66,\"result\":\"ABORTED\"},{\"number\":67,\"result\":\"SUCCESS\"}]";
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

    /**
     * refer to https://www.cnblogs.com/wgx519/p/13688615.html
     * JsonNode是不可变的。为了创建JsonNode对象图，你需要改变图中的JsonNode实例，
     * 如设置属性值和子JsonNode实例。因为其不可变性，不能直接进行操作，替代的是其子类ObjectNode
     */
    @Test
    public void testWriteJsonNode() throws IOException {

        // 简单数据类型
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode parentNode = objectMapper.createObjectNode();
        parentNode.put("serviceId", "663a1396-f167-4bc6-b7b8-d93eaae2f6de");// put设置属性值为原始数据类型
        parentNode.put("metadataId", "4788a5c9-e706-463b-8b73-268650c34723");
        parentNode.put("field3", 999.999);

        ObjectNode emptyNode = objectMapper.createObjectNode();
        parentNode.put("emptyNode", emptyNode);

        // 对象数据类型
        ObjectNode requestPayload = objectMapper.createObjectNode();
        requestPayload.put("a", "#request['name']+#request['description']+22");
        requestPayload.put("sub-field2", true);

        // 数组数据类型
        ArrayNode arrayNode = objectMapper.createArrayNode();
        arrayNode.add("a");
        arrayNode.add("b");
        parentNode.set("array", arrayNode);

        // 数组里面套对象
        ArrayNode arrayNode2 = objectMapper.createArrayNode();
        ObjectNode arraySubObject = objectMapper.createObjectNode();
        arraySubObject.put("f", "#request['name']+2");
        arrayNode2.add(arraySubObject);
        parentNode.set("array2", arrayNode2);

        parentNode.set("child1", requestPayload);// set设置设置ObjectNode对象属性
        JsonNode jsonNode = parentNode;
        System.out.println(objectMapper.writeValueAsString(jsonNode));
    }

    /**
     * 反序列化成JsonNode 对象
     * 
     * @throws IOException
     */
    @Test
    public void readTree() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        // 加数组
        String array = "[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":null}]";
        ArrayNode arrayNode = (ArrayNode) objectMapper.readTree(array);
        // 加对象
        String object =
                "{\"text\":\"张三\",\"expensive\":6,\"body\":{\"rvNoNum\":23,\"rvNoRecords\":[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":null}]}}";
        ObjectNode objectNode = (ObjectNode) objectMapper.readTree(object);

        ObjectNode options = objectMapper.createObjectNode();
        options.set("array", arrayNode);
        options.set("object", objectNode);
        options.put("serviceId", "663a1396-f167-4bc6-b7b8-d93eaae2f6de");
        System.out.println(objectMapper.writeValueAsString(options));

    }

    /**
     * 递归读取,并修改叶子节点的值
     * refer to https://www.cnblogs.com/witpool/p/8444700.html
     *
     * @throws IOException
     */
    @Test
    public void readLeafNode() throws IOException {
        String str =
                "{\"date\":\"\\\"date1\\\"\",\"supplier\":{\"address\":\"\\\"add1\\\"\",\"name\":\"\\\"testY\\\"\",\"class\":\"\\\"class1\\\"\"},\"ID\":\"\\\"idaaa\\\"\",\"order\":{\"bom\":\"\\\"bom1\\\"\",\"bomType\":\"\\\"bomType1\\\"\",\"version\":\"\\\"version1\\\"\"}}";
        log.info("before translation : {}", str);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(str);
        jsonLeaf(jsonNode);
        log.info("after translation : {}", JacksonUtil.writeStr(jsonNode));

        String str2 =
                "[{\"date\":\"\\\"date2\\\"\",\"supplier\":{\"address\":\"\\\"add2\\\"\",\"stores\":[{\"assistances\":[\"\\\"aaa\\\"\",\"'version'\"]}],\"name\":\"\\\"testy2\\\"\",\"class\":\"\\\"class2\\\"\"},\"ID\":\"\\\"idbbb\\\"\",\"order\":{\"bom\":\"\\\"bom2\\\"\",\"bomType\":\"\\\"bomeType2\\\"\",\"version\":\"'version'\"}}]";
        log.info("before translation : {}", str2);
        ObjectMapper objectMapper1 = new ObjectMapper();
        JsonNode jsonNode2 = objectMapper1.readTree(str2);
        jsonLeaf(jsonNode2);
        log.info("after translation : {}", JacksonUtil.writeStr(jsonNode2));

    }

    /**
     * 递归读取叶子节点的值
     * refer to https://blog.csdn.net/dong8633950/article/details/109278537
     *
     * @throws IOException
     */
    @Test
    public void readWriteLeafNode() throws IOException {
        String str =
                "{\"date\":\"\\\"date1\\\"\",\"bool1\":true,\"int1\":1,\"double1\":1.98,\"supplier\":{\"address\":\"\\\"add1\\\"\",\"name\":\"\\\"testY\\\"\",\"class\":\"\\\"class1\\\"\"},\"ID\":\"\\\"idaaa\\\"\",\"order\":{\"bom\":\"\\\"bom1\\\"\",\"bomType\":\"\\\"bomType1\\\"\",\"version\":\"\\\"version1\\\"\"}}";
        String str3 =
                "{\"p4\":[\"#request['variables'][0]['name']\",\"abc\"],\"p1\":\"p1_test\",\"p5\":{\"date\":\"date1\",\"supplier\":{\"address\":\"add1\",\"name\":\"testY\",\"class\":\"class1\"},\"ID\":\"idaaa\",\"order\":{\"bom\":\"bom1\",\"bomType\":\"bomType1\",\"version\":\"version1\"}},\"p2\":2,\"p3\":true,\"p6\":[{\"date\":\"date2\",\"supplier\":{\"address\":\"add2\",\"name\":\"testy2\",\"class\":\"class2\"},\"ID\":\"idbbb\",\"order\":{\"bom\":\"bom2\",\"bomType\":\"bomeType2\",\"version\":\"#request['version']\"}}]}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(str3);
        jsonLeaf2(jsonNode);
        System.out.println(JacksonUtil.writeStr(jsonNode));

        // System.out.println("-----");
        // String str2 =
        // "[{\"date\":\"\\\"date2\\\"\",\"supplier\":{\"address\":\"\\\"add2\\\"\",\"name\":\"\\\"testy2\\\"\",\"class\":\"\\\"class2\\\"\"},\"ID\":\"\\\"idbbb\\\"\",\"order\":{\"bom\":\"\\\"bom2\\\"\",\"bomType\":\"\\\"bomeType2\\\"\",\"version\":\"'version'\"}}]";
        // ObjectMapper objectMapper1 = new ObjectMapper();
        // JsonNode jsonNode2 = objectMapper1.readTree(str2);
        // jsonLeaf2(jsonNode2);
        // System.out.println(JacksonUtil.writeStr(jsonNode2));
    }

    /**
     * read and write values
     *
     * @param node
     */
    public static void jsonLeaf2(JsonNode node) {


        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (entry.getValue() instanceof ValueNode && entry.getValue().isValueNode()) {
                    ValueNode t = (ValueNode) entry.getValue();
                    entry.setValue(new TextNode(t.asText() + "。。。。"));

                    // setPrimitiveValue(entry, t);
                } else {
                    jsonLeaf2(entry.getValue());
                }

            }
        }

        if (node.isArray()) {
            Iterator<JsonNode> it = node.iterator();
            while (it.hasNext()) {
                jsonLeaf2(it.next());

            }

        }
    }

    private static void setPrimitiveValue(Map.Entry<String, JsonNode> entry, ValueNode t) {
        switch (t.getNodeType()) {
            case BOOLEAN:
                entry.setValue(BooleanNode.valueOf(t.asBoolean()));
                break;
            case BINARY:
            case NUMBER:
                if (t.asText().contains(".")) {
                    entry.setValue(new DoubleNode(t.asDouble()));
                } else {
                    entry.setValue(new IntNode(t.asInt()));
                }
                break;
            case STRING:
            default:
                entry.setValue(new TextNode(t.asText()));

        }
    }
    
    /**
     * read and write values
     * 
     * @param node
     */
    public void jsonLeaf(JsonNode node) throws JsonProcessingException {
        if (node.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> it = node.fields();
            while (it.hasNext()) {
                Map.Entry<String, JsonNode> entry = it.next();
                if (entry.getValue() instanceof TextNode && entry.getValue().isValueNode()) {
                    TextNode t = (TextNode) entry.getValue();
                    stripValueNode(entry, t);
                } else {
                    jsonLeaf(entry.getValue());
                }
            }
        }


        if (node.isArray()) {
            Iterator<JsonNode> it = node.iterator();
            for (int i = 0; it.hasNext(); i++) {
                JsonNode arrayNode = it.next();
                if (arrayNode instanceof TextNode) {
                    String value = removeDoubleQuote(arrayNode.textValue());
                    ((ArrayNode) node).set(i, new TextNode(value));
                } else {
                    jsonLeaf(arrayNode);
                }
            }
        }
    }

    private void stripValueNode(Map.Entry<String, JsonNode> entry, TextNode t) throws JsonProcessingException {
        String textValue = t.asText();
        entry.setValue(new TextNode(removeDoubleQuote(textValue)));
    }

    public static final String DOUBLE_QUOTE = "\"";

    public String removeDoubleQuote(String var) {
        return var.startsWith(DOUBLE_QUOTE) && var.endsWith(DOUBLE_QUOTE) ? var.substring(1, var.length() - 1) : var;
    }


    // 不支持直接的array
    public static void parseExpression(JsonNode jsonNode) {
        Iterator<Map.Entry<String, JsonNode>> it = jsonNode.fields();
        while (it.hasNext()) {
            Map.Entry<String, JsonNode> entry = it.next();
            JsonNode entryValue = entry.getValue();
            if (entryValue.isObject()) {
                parseExpression(entryValue);
            } else if (entryValue.isArray()) {
                parseArrayNode(entryValue, entry);
            } else {


                String varA = entryValue.asText();
                log.debug("[ExtensionServiceExecutor#parseExpression]  value: {}", varA);

                // Object value;
                // if (!varA.contains("#")) {
                // // for constant value, no need to use spel
                // value = varA;
                // } else {
                // // for variable , use spel to parse value
                // // value = parser.parseExpression(varA).getValue(extensionPointContext, Object.class);
                // log.debug("[ExtensionServiceExecutor#parseExpression] Expression {}, value: {}", varA);
                // }
                if (Objects.nonNull(varA)) {
                    entry.setValue(TextNode.valueOf(varA.toString()));
                }
            }
        }
    }

    private static void parseArrayNode(JsonNode entryValue, Map.Entry<String, JsonNode> entry) {
        List<JsonNode> c = new ArrayList<>();
        for (JsonNode n : entryValue) {
            if (n.isObject()) {
                parseExpression(n);
            } else {
                String text = n.asText();
                // Object value = parser.parseExpression(text).getValue(extensionPointContext, Object.class);
                log.debug("[ExtensionServiceExecutor#parseArrayNode] Expression {}", n.asText());
                if (Objects.nonNull(text)) {
                    c.add(TextNode.valueOf(text.toString()));
                }
            }
        }
        if (CollectionUtils.isNotEmpty(c)) {
            entry.setValue(new ArrayNode(JsonNodeFactory.instance, c));
        }
    }

    @Test
    public void testParseExpression() throws IOException {

        ObjectMapper objectMapper = new ObjectMapper();

        // 对象里面简单类型
        String str0 = "{\"var1\":11}";
        JsonNode jsonNode0 = objectMapper.readTree(str0);
        parseExpression(jsonNode0);

        // 对象里面嵌套数组
        String str1 = "{\"var1\":[1,2,3]}";
        JsonNode jsonNode1 = objectMapper.readTree(str1);
        parseExpression(jsonNode1);

        String str3 =
                "{\"p4\":[\"#request['variables'][0]['name']\"],\"p1\":\"p1_test\",\"p5\":{\"date\":\"date1\",\"supplier\":{\"address\":\"add1\",\"name\":\"testY\",\"class\":\"class1\"},\"ID\":\"idaaa\",\"order\":{\"bom\":\"bom1\",\"bomType\":\"bomType1\",\"version\":\"version1\"}},\"p2\":2,\"p3\":true,\"p6\":[{\"date\":\"date2\",\"supplier\":{\"address\":\"add2\",\"name\":\"testy2\",\"class\":\"class2\"},\"ID\":\"idbbb\",\"order\":{\"bom\":\"bom2\",\"bomType\":\"bomeType2\",\"version\":\"#request['version']\"}}]}";
        JsonNode jsonNode3 = objectMapper.readTree(str3);
        parseExpression(jsonNode3);
    }

    /**
     * "options": {
     * "serviceId": "663a1396-f167-4bc6-b7b8-d93eaae2f6de",
     * "metadataId": "4788a5c9-e706-463b-8b73-268650c34723",
     * "requestPayload": {- action body parameters
     * "a": "#request['name']+#request['description']+22",
     * "b": {"x":12, "y":"#request['items'][0]['name']"},
     * "array":["13", "14", "#request['items'][1]['name']"],
     * "arrayObj":[{"f":15}, {"f":"#request['name']+2"}]
     * }
     * 
     * @throws IOException
     */
    @Test
    public void testWriteJsonNodeDemo() throws IOException {

        // 简单数据类型
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode options = objectMapper.createObjectNode();
        options.put("serviceId", "663a1396-f167-4bc6-b7b8-d93eaae2f6de");// put设置属性值为原始数据类型
        options.put("metadataId", "4788a5c9-e706-463b-8b73-268650c34723");

        ObjectNode requestPayload = objectMapper.createObjectNode();
        // 加简单数据类型
        requestPayload.put("a", "#request['name']+#request['description']+22");
        // 加对象数据类型
        ObjectNode b = objectMapper.createObjectNode();
        b.put("x", 12);
        b.put("y", "#request['items'][0]['name']");
        requestPayload.set("b", b);
        // 加数组
        ArrayNode array = objectMapper.createArrayNode();
        array.add("13");
        array.add("14");
        array.add("#request['items'][1]['name']");
        requestPayload.put("array", array);

        // 加数组对象
        ArrayNode arrayObj = objectMapper.createArrayNode();
        ObjectNode obj = objectMapper.createObjectNode();
        obj.put("f", 15);
        obj.put("f", "#request['name']+2");
        arrayObj.add(obj);
        requestPayload.put("arrayObj", arrayObj);
        options.put("requestPayload", requestPayload);
        System.out.println(objectMapper.writeValueAsString(options));

    }
    /**
     * JSON字符串反序列化为JsonNode对象
     * com.fasterxml.jackson.databind.JsonNode
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
    public void testReadJsonNodePath() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        String json =
                "{\"text\":\"张三\",\"expensive\":6,\"body\":{\"rvNoNum\":23,\"rvNoRecords\":[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":null}]}}";
        JsonNode jsonNode = objectMapper.readTree(json);
        String path = generateJsonPathArgumentFromJson(jsonNode, "8");
        System.out.println(path);
        // 输出text的值
        Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);

        String text = JsonPath.read(document, "$.text");

        // 输出rvNoNum的值
        int rvNoNum = JsonPath.read(document, "$.body.rvNoNum");
        int score = JsonPath.read(document, "$.body.rvNoRecords[1].score");

        Iterator<Map.Entry<String, JsonNode>> f = jsonNode.fields();
        while (f.hasNext()) {
            Map.Entry<String, JsonNode> entry = f.next();
            JsonNode value = entry.getValue();
            String key = entry.getKey();

            value.path(key);
            System.out.println("key is " + key + ",value is " + value.asToken().name());
        }
    }


    protected static String generateJsonPathArgumentFromJson(JsonNode jsonNode, String valueSearched) {
        if (jsonNode.isValueNode() && !jsonNode.asText().equals(valueSearched)) {
            return null;
        } else if (jsonNode.isContainerNode()) {
            if (jsonNode.isObject()) {
                Iterator<Map.Entry<String, JsonNode>> elements = jsonNode.fields();
                while (elements.hasNext()) {
                    Map.Entry<String, JsonNode> element = elements.next();
                    String res = generateJsonPathArgumentFromJson(element.getValue(), valueSearched);
                    if (res != null) {
                        return "." + element.getKey() + res;
                    }
                }
            } else {
                int i = 0;
                Iterator<JsonNode> elements = jsonNode.elements();
                while (elements.hasNext()) {
                    JsonNode element = elements.next();
                    String res = generateJsonPathArgumentFromJson(element, valueSearched);
                    if (StringUtils.isNotBlank(res)) {
                        return "[" + i + "]" + res;
                    }
                    i++;
                }
            }
        }

        return "";
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
    }

    @Test
    public void testReadJsonArray1() throws IOException {
        // 1. object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        String arrayJson = "[\n" + "    {\n" + "        \"oldMetadataId\": \"27caa165-8767-40b2-8ffa-0848cee9529e\",\n"
                + "        \"newMetadataId\": \"41ffd1f5-01c8-44d9-b483-95903c5ef760\",\n"
                + "        \"serviceId\": \"819a69a9-93b0-4549-83cd-20d2d6aaccbd\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"ffb7ad25-e943-4ad9-babe-f12ad1b7f518\",\n"
                + "        \"newMetadataId\": \"7d88a8bf-46cc-4a1f-9566-c76742099687\",\n"
                + "        \"serviceId\": \"1bcf730c-7e7f-42bf-961e-24a1b60c35fa\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"418ca4b6-ed68-4dac-a436-851b7ce5dfa5\",\n"
                + "        \"newMetadataId\": \"5832a262-4168-4a72-bb2a-e550b1e150e6\",\n"
                + "        \"serviceId\": \"d1fd5532-ba8f-4d7b-bff8-3b4d2bf6cae8\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"0384da73-03b6-4d04-8784-e393db99731f\",\n"
                + "        \"newMetadataId\": \"4f421596-4199-42f1-b874-4c28652e04b0\",\n"
                + "        \"serviceId\": \"01eb0ff7-d947-4807-962c-21c545b4b94b\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"3868a5c8-17f8-4790-866b-605bc7e95801\",\n"
                + "        \"newMetadataId\": \"d06d0328-8488-4ccf-9d66-f2d8b50beaa8\",\n"
                + "        \"serviceId\": \"ecdc7445-bca6-4455-ad35-9808dcda78bc\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"63bdce28-acf5-42bc-b691-8d98fb7329a8\",\n"
                + "        \"newMetadataId\": \"36eb4cbb-466f-458e-9926-aa0e89e1a9bb\",\n"
                + "        \"serviceId\": \"7ddc274a-6001-4063-a7b3-8c08aefca35f\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"db39a8fa-2999-43a5-addb-b2d211903541\",\n"
                + "        \"newMetadataId\": \"58f49375-2183-4cbc-9e1f-bf65d17308a7\",\n"
                + "        \"serviceId\": \"308b6419-340e-4476-8f94-7a98d3137246\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"d653bb25-f82b-4fd3-9562-d0177ee8d295\",\n"
                + "        \"newMetadataId\": \"c7dedd93-7d19-448f-b98c-f6936c863b0d\",\n"
                + "        \"serviceId\": \"340d22d8-2f2e-41ef-b62e-391e90dd375c\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"e48da30b-f51b-4f48-a9d5-760bea632af4\",\n"
                + "        \"newMetadataId\": \"e444e21e-4b24-4767-9032-c87aa500cbc5\",\n"
                + "        \"serviceId\": \"a1eff20d-cd3b-433d-bdbd-6e31372b2a55\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"e78f1e74-e8a6-4fb8-8e67-59befbbc3505\",\n"
                + "        \"newMetadataId\": \"298f638e-b6cd-4508-8c05-0696d5632619\",\n"
                + "        \"serviceId\": \"4efc869b-bb78-49b0-bc08-919fc54d72fb\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"959ccdb2-f3f7-49a6-8f98-97ca8969d4d9\",\n"
                + "        \"newMetadataId\": \"d163677d-7562-400a-b7b2-502558474bfe\",\n"
                + "        \"serviceId\": \"71ffa4cf-49e8-432b-a71e-2a48a94ce671\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"4313dc94-91ea-4327-a8ef-fb282a935105\",\n"
                + "        \"newMetadataId\": \"4494480f-dbbd-44a5-8d49-c1ff90a8bd7c\",\n"
                + "        \"serviceId\": \"a531d095-423c-4378-8680-7445e61cf43c\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"81c6fa1a-289f-47e6-918e-1b6171a3b64b\",\n"
                + "        \"newMetadataId\": \"214265ae-d159-4079-ad77-0df188fa0cfd\",\n"
                + "        \"serviceId\": \"4c3f000a-4f65-45be-9cf9-dfeab13b6d1b\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"b3cb4c31-df77-4cf6-a166-4f8e0f0dfbbd\",\n"
                + "        \"newMetadataId\": \"7f033c3a-f0e5-402d-b8f2-30c705422b24\",\n"
                + "        \"serviceId\": \"b61341fb-3a06-4853-a96e-65a10850ec80\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"8bad4a70-b137-448f-a79e-d336b0b3a600\",\n"
                + "        \"newMetadataId\": \"a5288575-1429-4939-83f0-e5f100a54fe3\",\n"
                + "        \"serviceId\": \"9e117c49-2af4-4ded-b10e-1e9e26fe687f\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"3ca73c62-eeaf-46dd-810a-f06e5e9c774b\",\n"
                + "        \"newMetadataId\": \"9eb8ec09-beb4-4b26-9623-305781ea81ab\",\n"
                + "        \"serviceId\": \"f781ecb1-29b4-4ef6-b94d-eb6f54e01f10\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"e894ae77-30e8-4282-b146-d351022d611d\",\n"
                + "        \"newMetadataId\": \"e12a1975-9f6e-486a-9476-0f9e046505f4\",\n"
                + "        \"serviceId\": \"5ab307af-87e3-48cb-a4ab-60d7801ec5a3\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"2052d3fd-d06b-4427-8a5b-6eab5b9a734c\",\n"
                + "        \"newMetadataId\": \"ebebda1d-f8cf-4ba1-927b-1ffacc51ed29\",\n"
                + "        \"serviceId\": \"62851e42-819b-4a55-be0e-7dedee8b0b5b\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"3b31cde3-962d-44e9-9fa4-6255d5e180ff\",\n"
                + "        \"newMetadataId\": \"bde58787-3c93-45a4-87d2-84e43616bf91\",\n"
                + "        \"serviceId\": \"a530f837-ec3f-4fc2-8efe-e6e959bd0410\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"0d1fd9df-d547-4862-ae0e-a090b37124f2\",\n"
                + "        \"newMetadataId\": \"742c51f5-1779-4750-9afc-533b9503975e\",\n"
                + "        \"serviceId\": \"c9689f0a-25ba-4573-a271-10dd3a2c5fc7\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"fede0bb8-27a0-49ab-acf5-d03eae446364\",\n"
                + "        \"newMetadataId\": \"e5848ede-bf44-4b82-bd69-600905942cf1\",\n"
                + "        \"serviceId\": \"9574011d-c650-441e-a071-65166c4a0b2e\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"418ca4b6-ed68-4dac-a436-851b7ce5dfa5\",\n"
                + "        \"newMetadataId\": \"5832a262-4168-4a72-bb2a-e550b1e150e6\",\n"
                + "        \"serviceId\": \"d1fd5532-ba8f-4d7b-bff8-3b4d2bf6cae8\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"cc990e76-9f46-43a2-99c7-77588d9b1fd4\",\n"
                + "        \"newMetadataId\": \"c6654985-fdf9-449d-bdb0-cf7b8e1145e6\",\n"
                + "        \"serviceId\": \"13126443-5aba-44ef-8f8a-4983fdcd95d3\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"1033709d-42de-4526-a12a-014566cf806d\",\n"
                + "        \"newMetadataId\": \"a60f926a-2b18-4477-a449-c0ac2aa24318\",\n"
                + "        \"serviceId\": \"85d08c50-4f90-4f4f-b96b-27c501610812\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"bf34ad5f-00eb-4de2-a011-836578b6b4d9\",\n"
                + "        \"newMetadataId\": \"70cf7f23-6597-446b-86f5-3f2b48919830\",\n"
                + "        \"serviceId\": \"fe1fb274-6138-4b38-8d6f-1c84e8c51708\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"1ebc4b04-9c66-4e51-9102-192cb80090ec\",\n"
                + "        \"newMetadataId\": \"550361e4-825e-4077-bc3c-01545bb08f22\",\n"
                + "        \"serviceId\": \"46dcaf7a-4ddb-4b31-a4eb-272e5b61a8a1\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"bd495a02-a761-4be2-a6d5-d2410966f38c\",\n"
                + "        \"newMetadataId\": \"847fe13e-24ca-4c0c-9842-210be45da723\",\n"
                + "        \"serviceId\": \"48fae6f0-2b95-4e56-9ade-ea58d89c40e2\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"4313dc94-91ea-4327-a8ef-fb282a935105\",\n"
                + "        \"newMetadataId\": \"4494480f-dbbd-44a5-8d49-c1ff90a8bd7c\",\n"
                + "        \"serviceId\": \"a531d095-423c-4378-8680-7445e61cf43c\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"33311fce-36c5-4a52-a630-c488efeff7d6\",\n"
                + "        \"newMetadataId\": \"56c747db-fbd8-4d15-b1ac-27a2f6956279\",\n"
                + "        \"serviceId\": \"aa3c4765-9426-4289-a21d-261f74b53eb6\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"1e9459cc-9782-43df-a49e-5c151146808f\",\n"
                + "        \"newMetadataId\": \"15ea0fd9-37bb-4da5-bada-c9686c7fee8d\",\n"
                + "        \"serviceId\": \"d8b971b9-63d4-4e12-8455-1da267d20e58\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"70c059f8-fb5f-47cf-be10-96c584c7e5ca\",\n"
                + "        \"newMetadataId\": \"8a930ce3-1d59-44b8-9058-9033bb112e3f\",\n"
                + "        \"serviceId\": \"3350b348-8a3d-49d8-beca-fc96dbed856c\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"68cb256d-08fc-457f-8cf1-651b0bf63338\",\n"
                + "        \"newMetadataId\": \"f66f17cc-5d2f-42f4-b7d3-c992d1a1ff56\",\n"
                + "        \"serviceId\": \"197744c6-255b-459a-a600-ba102a16dcfc\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"4bceeaef-91f8-4152-9671-2b280c876c82\",\n"
                + "        \"newMetadataId\": \"4c9db1f1-d416-45f9-8f3d-45e203aee9a1\",\n"
                + "        \"serviceId\": \"e6ce8bd8-e199-402c-be21-a3d35adc8980\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"df58449f-b4be-4cc0-87ef-241c2774e40e\",\n"
                + "        \"newMetadataId\": \"213b8acf-8a91-4a20-8429-e5ddb565b7d9\",\n"
                + "        \"serviceId\": \"73a644fb-8f88-4f5c-b90f-d53dcc3e95a1\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"dc1cc321-6fc2-4122-b60c-da5cdea674a1\",\n"
                + "        \"newMetadataId\": \"8dd0438f-30a7-4307-8d41-a17097563c9d\",\n"
                + "        \"serviceId\": \"50e6ceca-957a-41f6-8c07-34fdd16f7e31\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"bb7c29a8-0dba-4348-a5f8-9703621dbaa9\",\n"
                + "        \"newMetadataId\": \"79206bb1-615c-47ad-ac8b-801ec8011e7c\",\n"
                + "        \"serviceId\": \"496b1240-8f24-4f58-bc74-ca6ca1b34568\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"b1ca8b9c-c530-42b1-9c11-353a85119cc6\",\n"
                + "        \"newMetadataId\": \"4c5ed9b7-abdc-4380-8957-2c38b0888416\",\n"
                + "        \"serviceId\": \"3f312946-e194-4ed4-bd89-f3aa1a0a3875\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"4fa86fcb-f9eb-4da4-8395-f481cf9e5ccf\",\n"
                + "        \"newMetadataId\": \"e575fba6-9f3f-44b3-9f56-fbba705ad463\",\n"
                + "        \"serviceId\": \"b4dfb7fb-f151-4779-9c34-14e170d0a53d\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"271088c9-a4ef-4459-8a53-bcf52d1b11e1\",\n"
                + "        \"newMetadataId\": \"fa084134-f321-43c9-9cbc-b5754875030e\",\n"
                + "        \"serviceId\": \"3b5ff3f8-a8c3-40b8-b775-71208c20955d\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"d4e4584b-8809-442a-81a8-7c4fbafb853b\",\n"
                + "        \"newMetadataId\": \"f7c0d70f-f3fa-49ef-88e8-14530e5ed6c4\",\n"
                + "        \"serviceId\": \"c4babc1e-fa93-4cd4-9965-ad824a2c36f7\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"0dfc82d0-3461-443b-9f86-f3b0f0fad05f\",\n"
                + "        \"newMetadataId\": \"89984160-71e1-4089-9db7-bb030a128ccf\",\n"
                + "        \"serviceId\": \"86841783-8582-4bdf-b348-532527a69eca\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"0384da73-03b6-4d04-8784-e393db99731f\",\n"
                + "        \"newMetadataId\": \"4f421596-4199-42f1-b874-4c28652e04b0\",\n"
                + "        \"serviceId\": \"01eb0ff7-d947-4807-962c-21c545b4b94b\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"d6c02538-d33d-4bcc-aa14-0c78e59f3518\",\n"
                + "        \"newMetadataId\": \"59fa05e8-c429-4c1b-b0d1-c1f7a3687d10\",\n"
                + "        \"serviceId\": \"5710a28e-2537-486e-92d5-1e75462f75ed\"\n" + "    },\n" + "    {\n"
                + "        \"oldMetadataId\": \"5040a903-ecce-4d5f-b7fa-17cfae2c8c1d\",\n"
                + "        \"newMetadataId\": \"601a6abd-c891-4fbd-9503-0d1ccaabf552\",\n"
                + "        \"serviceId\": \"a01f3e06-066c-4580-9bab-058ac5b2309e\"\n" + "    }\n" + "]";
        JsonNode jsonNode = objectMapper.readTree(arrayJson);
        // 如果是一个JsonNode数组，使用jsonNode.elements();读取数组中每个node
        Iterator<JsonNode> elements = jsonNode.elements();
        log.info("object mapper parsing array");
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        while (elements.hasNext()) {
            JsonNode node = elements.next();
            String serviceId = node.get("serviceId").asText();
            if (list.contains(serviceId)) {
                log.info("duplicated service id {}", serviceId);
            }
            list.add(serviceId);
            set.add(serviceId);
        }
        log.info("list size is {}, set size is {}", list.size(), set.size());

    }

    @Test
    public void testReadJsonArray2() throws IOException {
        // 1. object mapper
        ObjectMapper objectMapper = new ObjectMapper();
        String arrayJson = "[{\n" + "\t\"oldMetadataId\": \"c3e4de4a-4d4c-4f35-a1f1-4e0008dad306\",\n"
                + "\t\"newMetadataId\": \"c3e4de4a-4d4c-4f35-a1f1-4e0008dad306\",\n"
                + "\t\"serviceId\": \"83d35f91-bd22-4876-9a35-6fd4be760c30\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"a04912e2-5e28-42d3-b48e-66cce28674fe\",\n"
                + "\t\"newMetadataId\": \"a04912e2-5e28-42d3-b48e-66cce28674fe\",\n"
                + "\t\"serviceId\": \"eb2e05e9-467a-435d-90a5-72163dc4f869\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"d662a022-989e-4af4-b26b-f0aa246091c0\",\n"
                + "\t\"newMetadataId\": \"d662a022-989e-4af4-b26b-f0aa246091c0\",\n"
                + "\t\"serviceId\": \"2a1a51a4-0415-4958-b6cc-3c40804d45a1\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"0d10e3ea-b196-427e-8c1c-eb9e14d7e80f\",\n"
                + "\t\"newMetadataId\": \"0d10e3ea-b196-427e-8c1c-eb9e14d7e80f\",\n"
                + "\t\"serviceId\": \"f2cd519b-d2db-45b8-af52-233429f5c4aa\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"7063689a-8882-4632-afd9-d883abab310b\",\n"
                + "\t\"newMetadataId\": \"7063689a-8882-4632-afd9-d883abab310b\",\n"
                + "\t\"serviceId\": \"3b481012-3957-4367-ae6c-4db7385005c6\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"8cf6adc4-7e2a-4dd8-9223-300496d296a0\",\n"
                + "\t\"newMetadataId\": \"8cf6adc4-7e2a-4dd8-9223-300496d296a0\",\n"
                + "\t\"serviceId\": \"447a2f9a-3e39-478d-a89f-3d949760e97c\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"b8f4c583-18d6-483a-8808-bcda3d2ccb08\",\n"
                + "\t\"newMetadataId\": \"b8f4c583-18d6-483a-8808-bcda3d2ccb08\",\n"
                + "\t\"serviceId\": \"88b3e568-8c10-4802-bf76-dc724d6841e8\",\n" + "\t\"status\": null\n" + "}, {\n"
                + "\t\"oldMetadataId\": \"3f1a438b-d2f3-4870-a249-313765f99d4f\",\n"
                + "\t\"newMetadataId\": \"3f1a438b-d2f3-4870-a249-313765f99d4f\",\n"
                + "\t\"serviceId\": \"2e74d63c-6c92-4183-a91d-0310e65bb30c\",\n" + "\t\"status\": null\n" + "}]";
        JsonNode jsonNode = objectMapper.readTree(arrayJson);
        // 如果是一个JsonNode数组，使用jsonNode.elements();读取数组中每个node
        Iterator<JsonNode> elements = jsonNode.elements();
        log.info("object mapper parsing array");
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        while (elements.hasNext()) {
            JsonNode node = elements.next();
            String serviceId = node.get("serviceId").asText();
            if (list.contains(serviceId)) {
                log.info("duplicated service id {}", serviceId);
            }
            if (StringUtils.isBlank(node.get("newMetadataId").asText())) {
                log.error("blank newMetadataId for service {}", serviceId);
            }
            list.add(serviceId);
            set.add(serviceId);
        }
        String result = JacksonUtil.writeStr(set);
        log.info("list size is {}, set size is {},serives are  {}", list.size(), set.size(), result.replace("\"", "\'"));

    }

    /**
     * use gson加入简单属性，array 属性和复杂属性
     */
    @Test
    public void testJsonObject() {
        com.google.gson.JsonObject jsonObject = new com.google.gson.JsonObject();
        // 加入简单属性
        jsonObject.addProperty("number", 1);
        jsonObject.addProperty("isValid", false);
        jsonObject.addProperty("name", "richard");

        // 加入json array
        jsonObject.add("stringArray", JsonParser.parseString("[\"abc\",\"abcdd\"]").getAsJsonArray());
        jsonObject.add("intArray", JsonParser.parseString("[1,3,4]").getAsJsonArray());
        jsonObject.add("boolArray", JsonParser.parseString("[false,true]").getAsJsonArray());

        // 加入json Object
        jsonObject.add("object", JsonParser.parseString("{\"triggerFrom\":\"Timer\",\"intValue\":1}").getAsJsonObject());
        System.out.println(jsonObject.toString());

        // 获取JsonObject中指定key值对应的JsonArray对象：
        String json = "{\"pids\":[\"1\",\"2\",\"3\"]}";
        System.out.println(JsonParser.parseString(json).getAsJsonObject().getAsJsonArray("pids").get(0).getAsString());

        // test 2
        // 外层是json,里面是array
        // {"ServiceMapping":[{"number":1,"isValid":false,"name":"richard"}]}
        com.google.gson.JsonObject jsonObject1 = new com.google.gson.JsonObject();
        JsonArray array = new JsonArray();
        com.google.gson.JsonObject jsonObject2 = new com.google.gson.JsonObject();
        jsonObject2.addProperty("number", 1);
        jsonObject2.addProperty("isValid", false);
        jsonObject2.addProperty("name", "richard");
        array.add(jsonObject2);
        jsonObject1.add("ServiceMapping", array);
        System.out.println(jsonObject1.toString());

        // test 3
        // 空json : {}
        com.google.gson.JsonObject jsonObject3 = new com.google.gson.JsonObject();
        System.out.println(jsonObject3.toString());

    }



}


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
class Car {

    private String color;
    private String type;
}


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
class Object1 {
    private String number;
    private String result;
}
