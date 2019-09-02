package com.uad2.application.message;

import com.uad2.application.config.PropertiesBundle;
import com.uad2.application.config.WebConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @PostMapping("/message/sendMessage")
    public Object sendMessage(
            @RequestParam String targetNumber,
            @RequestParam String content){
        int reserve = 1;
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("uID",PropertiesBundle.MESSAGE_UID);
            paramMap.put("uPWD",PropertiesBundle.MESSAGE_UPWD);
            paramMap.put("reserve",reserve);
            paramMap.put("phone",PropertiesBundle.MESSAGE_HOST_NUMBER);
            paramMap.put("recv_number",targetNumber);
            paramMap.put("msg",content);

            StringBuilder postData = new StringBuilder();
            for(Map.Entry<String,Object> param : paramMap.entrySet()) {
                if(postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)new URL(PropertiesBundle.MESSAGE_URL).openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes); // POST 호출

            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));

            String inputLine;
            while((inputLine = in.readLine()) != null) { // response 출력
                System.out.println(inputLine);
            }
            in.close();
        }catch (Exception e){
            e.printStackTrace();
        }
        return "test";
    }
}
