package kr.pe.junho85.spring;

import kr.pe.junho85.spring.ch06.Hello;
import kr.pe.junho85.spring.ch06.HelloTarget;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.framework.ProxyFactoryBean;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class DynamicProxyTest {
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); //부가기능을 추가할 대상(=타깃) 설정
        pfBean.addAdvice(new UppercaseAdvice()); //부가기능(=어드바이스) 추가

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("toby"), is("HELLO TOBY"));

    }

    static class UppercaseAdvice implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            String ret = (String) invocation.proceed(); //리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다. MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문
            return ret.toUpperCase(); //부가기능 적용
        }
    }

}
