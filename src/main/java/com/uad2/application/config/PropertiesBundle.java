package com.uad2.application.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:/config.properties")//classpath : test 실행 가능
public class PropertiesBundle {

    public static String MESSAGE_URL;
    public static String MESSAGE_UID;
    public static String MESSAGE_UPWD;
    public static String MESSAGE_HOST_NUMBER;
    @Value("${message.url}")
    public void setMessageUrl(String messageUrl){
        PropertiesBundle.MESSAGE_URL = messageUrl;
    }
    @Value("${message.uId}")
    public void setMessageUId(String messageUId){
        PropertiesBundle.MESSAGE_UID = messageUId;
    }
    @Value("${message.uPwd}")
    public void setMessageUPwd(String messageUPwd){
        PropertiesBundle.MESSAGE_UPWD = messageUPwd;
    }
    @Value("${message.hostNumber}")
    public void setMessageHostNumber(String messageHostNumber){
        PropertiesBundle.MESSAGE_HOST_NUMBER = messageHostNumber;
    }
    /*
    message.uId=SJCE
    message.uPwd=sjce1234
    message.hostPhoneNumber=01094736496
     */
    /*
    @Value("${uId}")
    public static String uId;
    @Value("${uPwd}")
    public static String uPwd;
    @Value("${hostPhoneNumber}")
    public static String hostPhoneNumber;
     */
}
