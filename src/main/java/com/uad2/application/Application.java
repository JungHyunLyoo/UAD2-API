package com.uad2.application;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//스프링부트 프로젝트 서버 실행의 시작지점
public class Application implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(Application.class);
        springApplication.run(args);
        //springApplication 변수를 통해서 시작 시점에 다양한 작업 추가 가능
    }
    @Override
    public void run(String... args) throws Exception {
        //서버 실행이 완료(?)되고 무조건 실행되는 메소드
        //콘솔에 찍어봐야하는 테스트 코드 실행 가능
    }
}
