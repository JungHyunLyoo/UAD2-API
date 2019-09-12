package com.uad2.application;

/*
 * @USER JungHyun
 * @DATE 2019-09-09
 */

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(JUnitParamsRunner.class)
public class MemberTests {
    private Object[] paramsForTestFree(){
        return new Object[]{
                new Object[] {0},
                new Object[] {0}
        };
    }
    @Test
    @Parameters(method = "paramsForTestFree")
    public void testTest(int n1){
        Assert.assertEquals(n1,0);
    }
}
