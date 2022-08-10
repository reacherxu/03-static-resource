package com.richard.demo.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/8/1 15:27 richard.xu Exp $
 */
@Data
public class Family {

    @NotBlank(message = "father can not be blank")
    private  String father;
    @Size(message = "length can not longer than 10",max=10)
    private String mother;
    private String son;
}
