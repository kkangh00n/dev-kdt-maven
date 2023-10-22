package org.prgms;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


interface Calculator{
    int add(int a, int b);
}

class CalculatorImpl implements Calculator{
    @Override
    public int add(int a, int b) {
        return a+b;
    }
}

//calculatorImpl의 Proxy 객체 -> InvocationHandler를 구현
class LoggingInvocationHandler implements InvocationHandler{
    private static final Logger log = LoggerFactory.getLogger(LoggingInvocationHandler.class);

    //Calculator
    private final Object target;

    public LoggingInvocationHandler(Object target){
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        //부가 로직 추가
        log.info("{} executed in {}", method.getName(), target.getClass().getCanonicalName());
        return method.invoke(target, args);
    }
}
public class JdkProxyTest {
    private static final Logger log = LoggerFactory.getLogger(JdkProxyTest.class);

    public static void main(String[] args) {
        CalculatorImpl calculator = new CalculatorImpl();
        //Proxy 객체 생성
        Calculator proxyInstance = (Calculator) Proxy.newProxyInstance(
            LoggingInvocationHandler.class.getClassLoader(),
            new Class[]{Calculator.class}, new LoggingInvocationHandler(calculator));
        int result = proxyInstance.add(1, 2);
        log.info("Add -> {}", result);
    }
}
