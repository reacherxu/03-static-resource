/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.controller;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring boot 文件上传
 * 
 * @author richard.xu03@sap.com
 * @version $Id: FileUploadController.java, v 0.1 May 23, 2020 5:24:56 PM richard.xu Exp $
 */
// @Controller
@RestController // 此类下所有的方法返回值都会做json格式的转换，方法名就不用添加ResponseBody注解了 == controller + ResponseBody
@Slf4j
public class FileUploadController {

    /**
     * action 要和路径名相同
     * 
     * @param file
     * @return
     * @throws IllegalStateException
     * @throws IOException
     */
    @RequestMapping(value = "/upload", method = RequestMethod.PUT)
    public Map<String, Object> fileUpload(@RequestParam(value = "file1") MultipartFile file) throws IllegalStateException, IOException {
        log.info("file name is {}", file.getOriginalFilename());
        file.transferTo(new File("/Users/i350644/Coding/eclipse-workspace/" + file.getOriginalFilename()));
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("msg", "ok");
        return map;
    }
}
