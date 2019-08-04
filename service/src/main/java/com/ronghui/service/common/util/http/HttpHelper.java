package com.ronghui.service.common.util.http;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/**
 * @auther: dongyang_wu
 * @date: 2019/4/1 15:31
 * @description:
 */
@Component("HttpHelper")
public class HttpHelper {
    @Autowired
    private RestTemplate restTemplate;
    private final String APPID = "wx9b3bb1e2cda0e27d";
    private final String SECRET = "cd23d18efd7fe26605a99fd00bb656a4";
    // http://123.206.73.65:8001/upload/
    @Value("${zimg.djangoUrl}")
    private String djangoUrl = "12313";

    public ResponseEntity get(String url, Map headers, Map<String, Object> paramMapArgs) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(null, httpHeaders);
        ResponseEntity result;
        if (paramMapArgs != null)
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class, paramMapArgs);
        else
            result = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);
        return result;
    }

    public ResponseEntity post(String url, Map headers, Map<String, Object> bodyMapArgs) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.setAll(bodyMapArgs);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);
        ResponseEntity result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        return result;
    }

    public ResponseEntity postWithResultString(String url, Map headers, Map<String, Object> bodyMapArgs) {
        HttpHeaders httpHeaders = new HttpHeaders();
        if (headers != null) {
            httpHeaders.setAll(headers);
        }
        MultiValueMap<String, Object> bodyMap = new LinkedMultiValueMap<>();
        bodyMap.setAll(bodyMapArgs);
        HttpEntity<MultiValueMap<String, Object>> httpEntity = new HttpEntity<>(bodyMap, httpHeaders);
        ResponseEntity result = restTemplate.exchange(url, HttpMethod.POST, httpEntity, String.class);
        return result;
    }

    public ResponseEntity wxLogin(String code) {
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=" + APPID + "&secret=" + SECRET + "&grant_type=authorization_code&js_code=" + code;
        ResponseEntity responseEntity = get(url, null, null);
        System.out.println(responseEntity.getBody().toString());
        return responseEntity;
    }

    public String getDjangoUrl() {
        return djangoUrl;
    }
}
