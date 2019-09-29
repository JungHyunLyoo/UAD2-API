package com.uad2.application.exception;
/*
 * @USER JungHyun
 * @DATE 2019-09-22
 * @DESCRIPTION
 */

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ExceptionAdvice{
    private Map<String,String> getErrorForm(RuntimeException e) {
        Map<String,String> returnMap = new HashMap<>();
        returnMap.put("error",e.toString());
        returnMap.put("message",e.getMessage());
        return returnMap;
    }

    @ExceptionHandler(value = { RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> runtimeException(RuntimeException e) {
        return getErrorForm(e);
        /*Map<String,String> returnMap = new HashMap<>();
        returnMap.put("error",e.toString());
        returnMap.put("message",e.getMessage());
        return returnMap;*/
    }

    @ExceptionHandler(value = { TestException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> testException(TestException e) {
        return getErrorForm(e);
        /*Map<String,String> returnMap = new HashMap<>();
        returnMap.put("error",e.toString());
        returnMap.put("message",e.getMessage());
        return returnMap;*/
    }

    @ExceptionHandler(value = { MemberException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, String> memberException(MemberException e) {
        return getErrorForm(e);
        /*Map<String,String> returnMap = new HashMap<>();
        returnMap.put("error",e.toString());
        returnMap.put("message",e.getMessage());
        return returnMap;*/
    }
}
