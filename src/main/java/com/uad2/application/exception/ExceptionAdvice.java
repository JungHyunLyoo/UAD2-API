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
public class ExceptionAdvice {
    //등록한 익셉션이 발생하면, 해당 ExceptionHandler에
    //매핑되어 알맞은 에러 처리 가능

    // default error form
    private Map<String,String> getErrorForm(RuntimeException e) {
        e.printStackTrace();
        Map<String,String> returnMap = new HashMap<>();
        returnMap.put("error",e.toString());
        returnMap.put("message",e.getMessage());
        return returnMap;
    }

    @ExceptionHandler(value = { RuntimeException.class })
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String,String> runtimeException(RuntimeException e) {
        return getErrorForm(e);
    }

    @ExceptionHandler(value = { ClientException.class })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String,String> clientException(ClientException e) {
        return getErrorForm(e);
    }

    @ExceptionHandler(value = { ForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public Map<String,String> forbiddenException(ForbiddenException e) {
        return getErrorForm(e);
    }
}
