/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.controller;

import java.util.HashMap;
import java.util.Map;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.richard.demo.model.User;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: SwaggerController.java, v 0.1 May 28, 2020 3:34:53 PM richard.xu Exp $
 */
@Tag(name = "/swagger", description = "swagger desc") // @Api() 用于类；表示标识这个类是swagger的资源
@Controller
@Slf4j
public class SwaggerController {

    // @ApiOperation() 用于方法；表示一个http请求的操作
    // @ApiParam() 用于方法，参数，字段说明；表示对参数的添加元数据（说明或是否必填等）
    @Operation(summary = "method api", description = "just an api in swagger")
    @ResponseBody
    @RequestMapping(value = "/swagger", method = RequestMethod.GET)
    public Map<String, Object> api(@Parameter(name = "id", example = "用户id", required = true) String id,
            @Parameter(name = "name", example = "用户name", required = false) String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }

    @Operation(summary = "update swagger user api", description = "an update action")
    @ApiResponses({@ApiResponse(responseCode = "400", description = "Invalid user"), @ApiResponse(responseCode = "409", description = "User conflict")})
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public int updateSwaggerUser(@RequestBody @Parameter(name = "user", example = "用户", required = true) User user) {
        int num = 0;
        log.info(user.getName());
        log.info("user age is {}", user.getAge());
        return num++;
    }
}
