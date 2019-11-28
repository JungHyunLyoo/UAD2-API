package com.uad2.application.message.controller;
/*
 * @USER JungHyun
 * @DATE 2019-09-12
 * @DESCRIPTION 메세지 컨트롤러
 */
import com.uad2.application.message.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.HashMap;
import java.util.Map;

@RestController
public class MessageController {

    @Autowired
    MessageService messageService;

    @PostMapping("/message/sendMessage")
    public ResponseEntity sendMessage(
            @RequestBody Map<String,Object> requestParam){
        String targetNumber = (String)requestParam.get("targetNumber");
        String content = (String)requestParam.get("content");

        String result = messageService.sendMessage(targetNumber,content);
        Map<String,Object> resultMap = new HashMap<>();
        resultMap.put("result",result);
        return ResponseEntity.ok(resultMap);

    }
}
