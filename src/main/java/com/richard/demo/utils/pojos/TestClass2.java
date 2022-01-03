package com.richard.demo.utils.pojos;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/1/3 5:14 PM richard.xu Exp $
 */

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TestClass2 {
    private String name;
    private Integer age;
    private Boolean married;
    private List<String> houses = new ArrayList<>();
}
