package com.richard.demo.utils.pojos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 * @author richard.xu03@sap.com
 * @version v 0.1 2022/1/3 5:12 PM richard.xu Exp $
 */
@Data
@SuperBuilder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SubClass1 {
    private String subPropertyA;
    private int subPropertyB;


}