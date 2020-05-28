/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: User.java, v 0.1 May 28, 2020 3:57:49 PM richard.xu Exp $
 */
@ApiModel(value = "user对象", description = "用户对象user")
@Data
public class User {
    @ApiModelProperty(value = "id", name = "user id", required = true)
    private int id;

    @ApiModelProperty(value = "用户名", name = "username", example = "xingguo")
    private String name;

    private int age;
    private String address;

}
