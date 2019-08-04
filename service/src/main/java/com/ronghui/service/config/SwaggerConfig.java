package com.ronghui.service.config;

import com.ronghui.service.Application;

import com.ronghui.service.context.ContextUtil;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.context.request.async.DeferredResult;
import springfox.documentation.builders.*;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
@ConditionalOnExpression("${swagger.enable:true}")
@Slf4j
public class SwaggerConfig {
    @Bean
    public Docket api() {
        //可以添加多个header或参数
        ParameterBuilder AUTH = new ParameterBuilder();
        AUTH.parameterType("header")
                .name(ContextUtil.AUTH)
                .description("header中Authorization字段用于登录获取token")
                .modelRef(new ModelRef("string"))
                //非必需，这里是全局配置，然而在登陆的时候是不用验证的
                .required(false).build();
        ParameterBuilder AUTHORIZATION = new ParameterBuilder();
        AUTHORIZATION.parameterType("header")
                .name(ContextUtil.AUTHORIZATION)
                .description("header中Authorization字段用于使用API的验证")
                .modelRef(new ModelRef("string"))
                //非必需，这里是全局配置，然而在登陆的时候是不用验证的
                .required(false).build();
        List<Parameter> aParameters = new ArrayList<Parameter>();
        aParameters.add(AUTH.build());
        aParameters.add(AUTHORIZATION.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .genericModelSubstitutes(DeferredResult.class)
                .useDefaultResponseMessages(false)
                .forCodeGeneration(false)
                .select()
                .apis(RequestHandlerSelectors.basePackage(Application.class.getPackage().getName()))
                .paths(PathSelectors.any())
                .build().apiInfo(apiInfo()).globalOperationParameters(aParameters);
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("容会科技后台API接口")
                .contact("Admin")
                .version("v0.01")
                .build();
    }
}