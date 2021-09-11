/**
 * SAP Inc.
 * Copyright (c) 1972-2018 All Rights Reserved.
 */
package com.richard.demo.utils;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import lombok.extern.slf4j.Slf4j;

/**
 * Convert object to json String
 * Note that: <br>
 * <ol>
 * there's some problem maybe enum
 * </ol>
 * <ol>
 * attribute in inner json may duplicate
 * </ol>
 * 
 * @author richard.xu03@sap.com
 * @version $Id: JacksonUtil.java, v 0.1 May 22, 2018 10:47:15 AM richard.xu Exp $
 */
@Slf4j
public class JacksonUtil {
    /**
     * convert object to json string
     * 
     * @param object
     * @return
     */
    public static String writeStr(Object object) {
        String output = null;

        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_INDEX);
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        mapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        try {
            output = mapper.writeValueAsString(object);
        } catch (Exception e) {
            log.warn("[JacksonUtil#writeStr] failed: {}.", e);
        }
        return output;

    }

    /**
     * convert json string to json node
     * 
     * @param jsonStr
     * @return
     * @throws IOException
     */
    public static JsonNode read(String jsonStr) {
        ObjectMapper mapper = new ObjectMapper();

        JsonNode rootNode = null;
        try {
            // create tree from JSON
            rootNode = mapper.readTree(jsonStr);
        } catch (Exception e) {
            log.warn("[JacksonUtil#read] failed: {}.", e);
        }
        return rootNode;
    }

}
