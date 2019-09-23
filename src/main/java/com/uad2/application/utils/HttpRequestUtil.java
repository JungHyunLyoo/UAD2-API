package com.uad2.application.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION http request 모듈
 */
@Component
public class HttpRequestUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpRequestUtil.class);
    private static RestTemplate restTemplate = new RestTemplate();

    public static String getRequest(String url){
        return getRequest(url,null,StandardCharsets.UTF_8);
    }
    public static String getRequest(String url, Charset charset) {
        return getRequest(url,null,charset);
    }
    public static String getRequest(String url, Map<String,Object> paramMap){
        return getRequest(url,paramMap,StandardCharsets.UTF_8);
    }
    public static String getRequest(String url, Map<String,Object> paramMap, Charset charset) {
        MultiValueMap<String, String> requestParameters = new LinkedMultiValueMap<>();
        if( paramMap != null ){
            requestParameters.setAll(paramMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue()))));
        }

        UriComponentsBuilder uriComponentsBuilder = UriComponentsBuilder
                                                        .fromHttpUrl(url)
                                                        .queryParams(requestParameters)
                                                        .encode(charset);

        UriComponents uriComponents = uriComponentsBuilder.build();

        ResponseEntity<String> responseEntity = restTemplate.getForEntity(uriComponents.toUri(), String.class);

        return responseEntity.getBody();
    }

    public static String postRequest(String url, Map<String,Object> paramMap){
        return postRequest(url,paramMap,StandardCharsets.UTF_8, MediaType.APPLICATION_JSON);
    }
    public static String postRequest(String url, Map<String,Object> paramMap,Charset charset){
        return postRequest(url,paramMap,charset,MediaType.APPLICATION_JSON);
    }
    public static String postRequest(String url, Map<String,Object> paramMap,MediaType mediaType){
        return postRequest(url,paramMap,StandardCharsets.UTF_8,mediaType);
    }
    public static String postRequest(String url, Map<String,Object> paramMap,Charset charset,MediaType type){
        ResponseEntity<String> responseEntity = null;

        if(MediaType.APPLICATION_FORM_URLENCODED.equals(type)){
            MultiValueMap<String, String> keyValueMap = new LinkedMultiValueMap<>();
            if( paramMap != null ){
                keyValueMap.setAll(paramMap.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> String.valueOf(e.getValue()))));
            }
            responseEntity = restTemplate.postForEntity(url,keyValueMap,String.class);
        }
        else if(MediaType.APPLICATION_JSON.equals(type)){
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(new MediaType("application", "json", charset));
            HttpEntity entity = new HttpEntity(paramMap, headers);
            responseEntity = restTemplate.postForEntity(url,entity,String.class);
        }

        return responseEntity.getBody();
    }
}
