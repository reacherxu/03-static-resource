/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import com.richard.demo.model.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: SwaggerController.java, v 0.1 May 28, 2020 3:34:53 PM richard.xu Exp $
 */
@Api(value = "/swagger", tags = "swagger desc") // @Api() 用于类；表示标识这个类是swagger的资源
@Controller
@Slf4j
public class SwaggerController {

    // @ApiOperation() 用于方法；表示一个http请求的操作
    // @ApiParam() 用于方法，参数，字段说明；表示对参数的添加元数据（说明或是否必填等）
    @ApiOperation(value = "method api", notes = "just an api in swagger")
    @ResponseBody
    @RequestMapping(value = "/swagger", method = RequestMethod.GET)
    public Map<String, Object> api(@ApiParam(name = "id", value = "用户id", required = true) String id,
            @ApiParam(name = "name", value = "用户name", required = false) String name) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("id", id);
        map.put("name", name);
        return map;
    }

    @ApiOperation(value = "update swagger user api", notes = "an update action")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid user"), @ApiResponse(code = 409, message = "User conflict")})
    @RequestMapping(value = "update", method = RequestMethod.POST)
    @ResponseBody
    public int updateSwaggerUser(@RequestBody @ApiParam(name = "user", value = "用户", required = true) User user) {
        int num = 0;
        log.info(user.getName());
        log.info("user age is {}", user.getAge());
        return num++;
    }
}
