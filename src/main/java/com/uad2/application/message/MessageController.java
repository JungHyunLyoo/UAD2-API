package com.uad2.application.message;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION 메세지 컨트롤러
 */
import com.uad2.application.config.PropertiesBundle;
import com.uad2.application.utils.HttpRequestUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @PostMapping("/message/sendMessage")
    public ResponseEntity sendMessage(
            @RequestParam String targetNumber,
            @RequestParam String content){
        int reserve = 1;
        Map<String,Object> resultMap = new HashMap<>();
        try {
            Map<String,Object> paramMap = new HashMap<String,Object>();
            paramMap.put("uID",PropertiesBundle.MESSAGE_UID);
            paramMap.put("uPWD",PropertiesBundle.MESSAGE_UPWD);
            paramMap.put("reserve",reserve);
            paramMap.put("phone",PropertiesBundle.MESSAGE_HOST_NUMBER);
            paramMap.put("recv_number",targetNumber);
            paramMap.put("msg",content);
            String result = HttpRequestUtil.postRequest(PropertiesBundle.MESSAGE_URL,paramMap, MediaType.APPLICATION_FORM_URLENCODED);

            if("CODE 0 : 발신 성공".equals(result)){
                resultMap.put("result","success");
            }
            else{
                resultMap.put("result","message api call error");
            }
        }catch (Exception e){
            resultMap.put("result","server error");
            e.printStackTrace();
        }
        return ResponseEntity.ok(resultMap);
    }
}
