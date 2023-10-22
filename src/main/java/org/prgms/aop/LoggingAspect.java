package org.prgms.aop;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

//AOP 클래스 파일
@Aspect
@Component
public class LoggingAspect {

    private final Logger log = LoggerFactory.getLogger(LoggingAspect.class);


    //포인트 컷 관리 2.
    @Pointcut("execution(public * org.prgms.voucher..*.*(..))")
    public void pointCut(){};


    //    //포인트 컷 관리 1.
    //execution -> 메서드를 실행시키는 시점
    //    @Around("execution(public * org.prgms.voucher..*.*(..))")

//    //포인트 컷 관리 2.
//    @Around("pointCut()")

    //포인트 컷 관리 3.
    @Around("org.prgms.CommonPointcut.pointCut()")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        log.info("Before method called. {}", joinPoint.getSignature().toString());
        Object result = joinPoint.proceed();
        log.info("After method called with result => {}", result);
        return result;
    }

}
