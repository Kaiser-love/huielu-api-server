package com.ronghui.service.config;

import com.ronghui.service.interceptor.UserInterceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.web.servlet.config.annotation.*;


@Configuration
public class WebConfigurer implements WebMvcConfigurer {
    private final UserInterceptor userInterceptor;

    @Autowired
    public WebConfigurer(UserInterceptor userInterceptor) {
        this.userInterceptor = userInterceptor;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/swagger-ui.html").addResourceLocations(
                "classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations(
                "classpath:/META-INF/resources/webjars/");
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //        registry.addInterceptor(licenseCheckInterceptor())
//                .excludePathPatterns("/index.html", "/", "/user/login", "/asserts/**", "/docs.html/**", "/user/role/**","/users/**")
//                .excludePathPatterns("/swagger-resources/**", "/webjars/**", "/v2/**", "/swagger-ui.html/**", "/license/installLicense", "/license/getServerInfos", "/license/generateLicense", "/license/downloadLicense")
//                .addPathPatterns("/**");
    }


//    @Override
//    protected void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(userInterceptor);
//    }

//    /**
//     * 修改自定义消息转化器
//     *
//     * @param converters 消息转换器列表
//     */
//    @Override
//    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
//        //创建fastJson消息转换器
//        FastJsonHttpMessageConverter fastJsonConverter = new FastJsonHttpMessageConverter();
//        //创建配置类
//        FastJsonConfig fastJsonConfig = new FastJsonConfig();
//        //过滤并修改配置返回内容
//        fastJsonConfig.setSerializerFeatures(
//                //    输出key是包含双引号
////                SerializerFeature.QuoteFieldNames,
//                SerializerFeature.WriteMapNullValue,
//                SerializerFeature.WriteNullNumberAsZero,
//                SerializerFeature.WriteNullListAsEmpty,
//                SerializerFeature.WriteNullStringAsEmpty,
//                SerializerFeature.WriteNullBooleanAsFalse,
//                //    Date的日期转换器
//                SerializerFeature.WriteDateUseDateFormat,
////                //    循环引用
//                SerializerFeature.DisableCircularReferenceDetect
//        );
//        //处理中文乱码问题
//        List<MediaType> fastMediaTypes = new ArrayList<>();
//        fastMediaTypes.add(MediaType.APPLICATION_JSON_UTF8);
//        fastJsonConverter.setSupportedMediaTypes(fastMediaTypes);
//        fastJsonConverter.setFastJsonConfig(fastJsonConfig);
//        //将fastjson添加到视图消息转换器列表内
//        converters.add(0,fastJsonConverter);
//    }

}