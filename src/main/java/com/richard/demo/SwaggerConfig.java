/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.oas.annotations.EnableOpenApi;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: SwaggerConfig.java, v 0.1 May 24, 2020 3:58:41 PM richard.xu Exp $
 */
@Configuration
@EnableOpenApi
public class SwaggerConfig {
    @Bean
    public Docket createRestApi() {
        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).enable(true).select()
                // 设置扫描的包名
                .apis(RequestHandlerSelectors.basePackage("com.richard.demo")).paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                // 文档内容配置信息
                .title("SpringBoot整合Swagger").description("这是一个简单的SpringBoot项目，基于Maven架构，SSM框架搭建")
                .termsOfServiceUrl("https://www.zhoutao123.com").version("1.0").build();
    }
}
