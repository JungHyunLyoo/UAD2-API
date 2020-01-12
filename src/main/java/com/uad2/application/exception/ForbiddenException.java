package com.uad2.application.exception;
/*
 * @USER JungHyun
 * @DATE 2019-09-22
 * @DESCRIPTION
 */

public class ForbiddenException extends RuntimeException{
    public ForbiddenException(String msg){
        super(msg);
    }
}
