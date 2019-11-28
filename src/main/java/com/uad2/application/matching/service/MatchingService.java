package com.uad2.application.matching.service;
/*
 * @USER zkdlwnfm
 * @DATE 2019-10-20
 */

import com.uad2.application.attendance.dto.AttendanceDto;
import com.uad2.application.matching.entity.Matching;
import com.uad2.application.matching.repository.MatchingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class MatchingService {
    @Autowired
    private MatchingRepository matchingRepository;

    /*public Matching createMatching(Matching matching) {
        return matchingRepository.save(matching);
    }*/

    public List<Matching> getMatchingByDate(String date) {
        return matchingRepository.findByMatchingDate(date);
    }

    public Matching updateMatching(Matching matching) {
        return matchingRepository.save(matching);
    }


    public Matching updateMatchingByAttendance(AttendanceDto.Request request){
        String availableTime = request.getAvailableTime();//null check
        String availableDate = request.getAvailableDate();
        int memberSeq = request.getMemberSeq();
        if(Objects.isNull(availableTime) || Objects.isNull(availableDate)){
            throw new RuntimeException("Need null check, availableTime : " + availableTime + ", availableDate : " + availableDate);
        }

        List<Matching> matchingList = matchingRepository.findByMatchingDate(availableDate);
        boolean isMatchingExist = Objects.nonNull(matchingList) && matchingList.size() > 0;
        if(isMatchingExist){
            Matching matching = matchingList.get(0);
            if(Objects.nonNull(matching)){
                //1.해당 매칭의 시간이 요청 시간에 포함 o, 해당 매칭에 요청 memseq 존재 x -> 해당 매칭에 요청 memseq 추가
                //2.해당 매칭의 시간이 요청 시간에 포함 x, 해당 매칭에 요청 memseq 존재 o -> 해당 매칭에 요청 memseq 삭제
                //3.해당 매칭의 시간이 요청 시간에 포함 o, 해당 매칭에 요청 memseq 존재 o -> 액션x(해당 매칭에 영향 x)
                //4.해당 매칭의 시간이 요청 시간에 포함 x, 해당 매칭에 요청 memseq 존재 x -> 액션x(해당 매칭에 영향 x)
                if(availableTime.contains(matching.getMatchingTime()) &&
                        !matching.getAttendMember().contains(Integer.toString(memberSeq))){
                    matching.setAttendMember(matching.getAttendMember() + "," + memberSeq);
                    return matching;
                }
                else if(!availableTime.contains(matching.getMatchingTime()) &&
                        matching.getAttendMember().contains(Integer.toString(memberSeq))){
                    System.out.println("delete attendance in matching");
                    matching.setAttendMember(matching.getAttendMember().replace("," + memberSeq,""));
                    matching.setAttendMember(matching.getAttendMember().replace("," + memberSeq,""));
                    matching.setAttendMember(matching.getAttendMember().replace("," + memberSeq,""));
                    return matching;
                }
            }
        }
        return null;
    }
}
