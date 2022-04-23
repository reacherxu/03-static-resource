package com.richard.demo.utils;

import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import lombok.extern.slf4j.Slf4j;

/**
 * refer to https://blog.csdn.net/revivedsun/article/details/103395128
 * 
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/4/2 12:15 PM richard.xu Exp $
 */
@Slf4j
public class SpELMain {

    static final String REGEX = "\\[[0-9]+\\]";
    static final Pattern PATTERN = Pattern.compile(REGEX);

    public static void main(String[] args) throws Exception {
        // String json =
        // "[{\"text\":\"张三\",\"expensive\":6,\"body\":{\"rvNoNum\":23,\"rvNoRecords\":[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":null}]}}]";
        // parseJson(json);

        String json1 =
                "{\"text\":\"张三\",\"expensive\":6,\"body\":{\"rvNoNum\":23,\"rvNoRecords\":[{\"score\":4,\"rvAddress\":\"2\",\"consignments\":null},{\"score\":8,\"rvAddress\":\"3\",\"consignments\":true}],\"rvFamily\":{\"sex\":\"male\",\"number\":6}}}";
        String json2 = "[\"'variables[0][\\\"defaultValue\\\"]'\", \"'name'\"]";
        String json3 =
                "{\"baseQuantity\":\"'var_int'\",\"baseUnitOfMeasure\":\"'script_structureArray[0][\\\"baseQuantity\\\"]'\",\"bom\":\"'script_out_int'\",\"bomType\":\"'script_structure[\\\"baseQuantity\\\"]'\",\"components\":[{\"alternates\":[{\"enabled\":\"'script_structure[\\\"components\\\"][0][\\\"assemblyOperationActivity\\\"][\\\"version\\\"]'\",\"material\":{\"material\":\"'var_part_match_structure[\\\"components\\\"][0][\\\"alternates\\\"][0][\\\"enabled\\\"]'\"}}]}]}";
        Map<String, Object> result = parseJson(json3);
        log.info("result is {}", JacksonUtil.writeStr(result));
    }

    private static Map<String, Object> parseJson(String json) throws IOException {
        Map<String, Object> flatResult = new HashMap<>();
        JsonReader reader = new JsonReader(new StringReader(json));
        reader.setLenient(true);
        while (true) {
            JsonToken token = reader.peek();
            switch (token) {
                case BEGIN_ARRAY:
                    reader.beginArray();
                    break;
                case END_ARRAY:
                    reader.endArray();
                    break;
                case BEGIN_OBJECT:
                    reader.beginObject();
                    break;
                case END_OBJECT:
                    reader.endObject();
                    break;
                case NAME:
                    reader.nextName();
                    break;
                case STRING:
                    String spath = reader.getPath();
                    String s = reader.nextString();
                    printAndAdd(spath, quote(s), flatResult);
                    break;
                case NUMBER:
                    String npath = reader.getPath();
                    String n = reader.nextString();
                    printAndAdd(npath, n, flatResult);
                    break;
                case BOOLEAN:
                    String bpath = reader.getPath();
                    boolean b = reader.nextBoolean();
                    printAndAdd(bpath, b, flatResult);
                    break;
                case NULL:
                    reader.nextNull();
                    break;
                case END_DOCUMENT:
                    return flatResult;
            }
        }
    }

    private static void printAndAdd(String path, Object value, Map<String, Object> flatResult) {
        // path = path.substring(1);
        // path = PATTERN.matcher(path).replaceAll("");
        System.out.println(path + ": " + value);
        flatResult.put(path, value);
    }

    static private String quote(String s) {
        return new StringBuilder().append('"').append(s).append('"').toString();
    }
}
