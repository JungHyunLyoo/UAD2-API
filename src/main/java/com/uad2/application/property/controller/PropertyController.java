package com.uad2.application.property.controller;
/*
 * @USER JunHo
 * @DATE 2019-12-22
 * @DESCRIPTION 프로퍼티 컨트롤러
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PropertyController {
    @Autowired
    Environment environment;

    @GetMapping(value = "/port")
    public ResponseEntity getPort() {
        String runningPort = environment.getProperty("server.port");
        return ResponseEntity.ok(runningPort);
    }
}
