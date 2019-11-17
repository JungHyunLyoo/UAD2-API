package com.uad2.application.attendance;
/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import com.uad2.application.exception.ClientException;
import com.uad2.application.member.dto.MemberDto;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class AttendanceValidator {
    public void validateGetAllAttendanceList(String date) {
        switch (date.length()){
            case 7:
                if(!Pattern.compile("(19|20)\\d{2}-(0[1-9]|1[012])").matcher(date).find()){
                    throw new ClientException("date pattern is wrong");
                }
                break;
            case 10:
                if(!Pattern.compile("(19|20)\\d{2}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])").matcher(date).find()){
                    throw new ClientException("date pattern is wrong");
                }
                break;
            default:
                throw new ClientException("date length is wrong");
        }
    }
}
