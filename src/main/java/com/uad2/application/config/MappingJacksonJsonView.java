package com.uad2.application.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MappingJacksonJsonView extends MappingJackson2JsonView {
    @Override
    public String getContentType() {
        return DEFAULT_CONTENT_TYPE;
    }
    @Override
    public void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request, HttpServletResponse response) throws Exception {

        super.getObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        super.getObjectMapper().configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        response.setContentType(getContentType());

        try{
            //jsonp 처리 추가 가능

            Object result = super.filterModel(model);
            Map<String,Object> resultMap = new HashMap<>();
            resultMap.put("code", response.getStatus());
            resultMap.put("message","message test");
            resultMap.put("path", request.getRequestURI());
            resultMap.put("data", result);
            resultMap.put("timestamp", new java.sql.Timestamp(new Date().getTime()));

            super.renderMergedOutputModel(resultMap, request, response);
        }catch(Exception e){
        }
    }
}