package com.uad2.application.calculation.controller;

import com.uad2.application.calculation.dto.CalculationDto;
import com.uad2.application.calculation.entity.Calculation;
import com.uad2.application.calculation.repository.CalculationRepository;
import com.uad2.application.calculation.service.CalculationService;
import com.uad2.application.common.annotation.Auth;
import com.uad2.application.common.enumData.Role;
import com.uad2.application.exception.ClientException;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.repository.MatchingRepository;
import com.uad2.application.member.entity.Member;
import com.uad2.application.member.service.MemberService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.util.*;

@RestController
public class CalculationController {
    private final CalculationRepository calculationRepository;
    private final CalculationService calculationService;
    private final MatchingRepository matchingRepository;
    private final MemberService memberService;
    private final ModelMapper modelMapper;

    @Autowired
    public CalculationController(CalculationRepository calculationRepository,
                                 CalculationService calculationService,
                                 MatchingRepository matchingRepository,
                                 MemberService memberService,
                                 ModelMapper modelMapper){
        this.calculationRepository = calculationRepository;
        this.calculationService = calculationService;
        this.matchingRepository = matchingRepository;
        this.memberService = memberService;
        this.modelMapper = modelMapper;
    }

    //@Auth(role = Role.ADMIN)
    @PostMapping(value = "/api/calculation", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity insertCalculation(@RequestBody CalculationDto.Request request){
        Matching matching = matchingRepository.findBySeq(request.getMatchingSeq());
        if(Objects.isNull(matching)){
            throw new ClientException("matching is not exist");
        }
        Calculation temp = modelMapper.map(request, Calculation.class);//있는 매칭 업데이트 테스트 되는지 판단 필요\
        temp.setMatching(matching);
        Calculation calculation = calculationService.saveCalculation(temp);
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("calculation",calculation);
        return ResponseEntity.ok().body(returnMap);
    }


    @Auth(role = Role.USER)
    @GetMapping(value = "/api/calculation/year/{year}/month/{month}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity getCalculation(HttpSession httpSession,
                                         @PathVariable int year,
                                         @PathVariable int month){

        Member member = (Member)httpSession.getAttribute("member");
        int memberSeq = member.getSeq();

        LocalDateTime strDts = LocalDateTime.of(year, month, 1, 0, 0, 0);
        LocalDateTime endDts = LocalDateTime.of(year, month+1, 1, 0, 0, 0);
        //년 넘어갈때 경우 체크
        List<Calculation> calculations = calculationRepository.findCalculationsByCreateAtAfterAndCreateAtBefore(strDts, endDts);
        int count = 0;
        int attendanceCnt = 0;
        for (Calculation calculation : calculations){
            if(calculation.getKind() == 0){

                List<Member> memberList = memberService.findBySeqList(
                        Arrays.stream(calculation.getMatching().getAttendMember().split(","))
                        .mapToInt(Integer::parseInt)
                        .toArray()
                );

                int weekAttdCntSum = 0;//각 매칭별 카운트 sum , 변수명 적잘하게 바꿔야함
                for(int i = 0; i < memberList.size(); i++) {
                    if(memberList.get(i).getSeq() == memberSeq) {
                        count++;
                    }
                    //회비납부대상자만 횟수 계산
                    if(memberList.get(i).getIsBenefit() == 0){
                        if(memberList.get(i).getIsWorker() == 1) {//직장인 enum값 설정 필요
                            weekAttdCntSum+=2;
                        }
                  else{
                            weekAttdCntSum+=1;
                        }
                    }
                }
                attendanceCnt += weekAttdCntSum;

            }
        }
        if(member.getIsWorker() == 1) { //회사원인 경우
            count *= 2 ; //두배 곱해줌
        }
        if(member.getIsBenefit() == 1) { //회비 면제
            count = 0;
        }
        Map<String, Object> returnMap = new HashMap<>();
        returnMap.put("calculationList",calculations);
        returnMap.put("count",count);
        returnMap.put("totalCount",attendanceCnt);
        return ResponseEntity.ok().body(returnMap);
    }
}
