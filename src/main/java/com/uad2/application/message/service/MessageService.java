package com.uad2.application.message.service;
/*
 * @USER JungHyun
 * @DATE 2019-11-28
 * @DESCRIPTION
 */

import com.uad2.application.config.PropertiesBundle;
import com.uad2.application.utils.HttpRequestUtil;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class MessageService {
    public String sendMessage(String targetNumber,String content){
        int reserve = 1;
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("uID", PropertiesBundle.MESSAGE_UID);
            paramMap.put("uPWD",PropertiesBundle.MESSAGE_UPWD);
            paramMap.put("reserve",reserve);
            paramMap.put("phone",PropertiesBundle.MESSAGE_HOST_NUMBER);
            paramMap.put("recv_number",targetNumber);
            paramMap.put("msg",content);
            String result = HttpRequestUtil.postRequest(PropertiesBundle.MESSAGE_URL,paramMap, MediaType.APPLICATION_FORM_URLENCODED);

            if("CODE 0 : 발신 성공".equals(result)){
                return "success";
            }
            else{
                return "message api call error";
            }
        }catch (Exception e){
            e.printStackTrace();
            return "server error";
        }
    }
}
