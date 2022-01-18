package com.richard.demo.utils.pojos;

import java.util.ArrayList;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/1/3 5:10 PM richard.xu Exp $
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class TestClass1 {
    private String name;
    private Integer age;
    private Boolean married;
    private SubClass1 sub;

    // 用于不使用默认null的builder的构造函数
    @Builder.Default
    private List<String> houses = new ArrayList<>();
}