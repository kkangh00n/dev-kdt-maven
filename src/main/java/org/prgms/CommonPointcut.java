package org.prgms;

import org.aspectj.lang.annotation.Pointcut;

//포인트 컷 관리 3.
public class CommonPointcut {

    @Pointcut("execution(public * org.prgms.voucher..*.*(..))")
    public void pointCut(){};

}
