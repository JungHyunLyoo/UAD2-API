package com.uad2.application.member.controller;

import com.uad2.application.member.repository.MemberRepository;
import com.uad2.application.member.entity.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class MemberController {
    //public static $AUTO_LOGIN_TIME = 86400*365;
    //insertMember
    //checkId
    //loginCheck
    //checkPwd
    //getMemberFromDB
    //login
    //logout
    //editProfile
    //uploadProfileImage
    static final Logger logger = LoggerFactory.getLogger(MemberController.class);

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping("/member")
    public Object Member(@RequestBody Member member){
        //폰번호로 존재 유무 체크
        /*

            $pwd=$request->get('pwd');
            $name=$request->get('name');
            $attd_cnt=0;
            $birth_day=$request->get('birth_day');
            $student_id=$request->get('student_id');
            $is_worker=$request->get('is_worker');
            $phone_number=$request->get('phone_number');
            $returnData->result=Member::insert(
                [
                    'id'            =>  $member_id,
                    'pwd'           =>  DB::raw("MD5('".$pwd."')") ,
                    'is_admin'      =>  0,
                    'is_benefit'      =>  0,
                    'name'          =>  $name,
                    'phone_number'  =>  $phone_number,
                    'attd_cnt'      =>  $attd_cnt,
                    'birth_day'     =>  $birth_day,
                    'student_id'    =>  $student_id,
                    'is_worker'     =>  $is_worker
                ]
            );
         */

        memberRepository.save(member);
        return "testtest";
    }
}