/**
 * SAP Inc.
 * Copyright (c) 1972-2020 All Rights Reserved.
 */
package com.richard.demo;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 *
 * @author richard.xu03@sap.com
 * @version $Id: SwaggerConfig.java, v 0.1 May 24, 2020 3:58:41 PM richard.xu Exp $
 */
@Configuration
public class SwaggerConfig {
//    @Bean
//    public Docket createRestApi() {
//        return new Docket(DocumentationType.OAS_30).apiInfo(apiInfo()).enable(true).select()
//                // 设置扫描的包名
//                .apis(RequestHandlerSelectors.basePackage("com.richard.demo")).paths(PathSelectors.any()).build();
//    }
//
//    private ApiInfo apiInfo() {
//        return new ApiInfoBuilder()
//                // 文档内容配置信息
//                .title("SpringBoot整合Swagger").description("这是一个简单的SpringBoot项目，基于Maven架构，SSM框架搭建")
//                .termsOfServiceUrl("https://www.zhoutao123.com").version("1.0").build();
//    }


    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info().title("Production Process Designer Api Documentation")
                        .description("SFD Api Documentation")
                        .version("1.0")
                        .contact(new Contact().name("PPD").url("https://dm-canary-dev-sfd-app.cfapps.sap.hana.ondemand.com/cdt/swagger").email(
                                "DL_53869A72FD84A06758000003@global.corp.sap")))
                .schemaRequirement("OAuthSecurity", getSecurityDefinition())
                ;
    }

    private SecurityScheme getSecurityDefinition() {
        SecurityScheme securityScheme = new SecurityScheme();
        securityScheme.setScheme("bearer");
        securityScheme.setType(io.swagger.v3.oas.models.security.SecurityScheme.Type.HTTP);
        securityScheme.setBearerFormat("JWT");
        securityScheme.setIn(io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER);
        return securityScheme;
    }
}
