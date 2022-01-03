/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;

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
