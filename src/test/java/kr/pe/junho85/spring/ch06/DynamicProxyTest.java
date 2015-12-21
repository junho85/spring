package kr.pe.junho85.spring.ch06;

import kr.pe.junho85.spring.ch06.Hello;
import kr.pe.junho85.spring.ch06.HelloTarget;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.junit.Test;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.framework.ProxyFactoryBean;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.aop.support.NameMatchMethodPointcut;

import static org.assertj.core.api.Assertions.*;

public class DynamicProxyTest {
    @Test
    public void proxyFactoryBean() {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(new HelloTarget()); // 부가기능을 추가할 대상(=타깃) 설정
        pfBean.addAdvice(new UppercaseAdvice()); // 부가기능(=어드바이스) 추가

        Hello proxiedHello = (Hello) pfBean.getObject();
        assertThat(proxiedHello.sayHello("toby")).isEqualTo("HELLO TOBY");
    }

    static class UppercaseAdvice implements MethodInterceptor {
        public Object invoke(MethodInvocation invocation) throws Throwable {
            // 리플렉션의 Method와 달리 메소드 실행 시 타깃 오브젝트를 전달할 필요가 없다.
            // MethodInvocation은 메소드 정보와 함께 타깃 오브젝트를 알고 있기 때문
            String ret = (String) invocation.proceed();
            // 부가기능 적용
            return ret.toUpperCase();
        }
    }

    @Test
    public void classNamePointcutAdvisor() {
        // 포인트컷 준비
        NameMatchMethodPointcut classMethodPointcut = new NameMatchMethodPointcut() {
            public ClassFilter getClassFilter() {
                return clazz -> clazz.getSimpleName().startsWith("HelloT");
            }
        };
        // sayH로 시작하는 메소드 이름을 가진 메소드만 선정한다
        classMethodPointcut.setMappedName("sayH*");

        // 테스트
        checkAdviced(new HelloTarget(), classMethodPointcut, true);

        class HelloWorld extends HelloTarget {};
        checkAdviced(new HelloWorld(), classMethodPointcut, false);

        class HelloToby extends HelloTarget {};
        checkAdviced(new HelloToby(), classMethodPointcut, true);
    }

    private void checkAdviced(HelloTarget target, Pointcut pointcut, boolean adviced) {
        ProxyFactoryBean pfBean = new ProxyFactoryBean();
        pfBean.setTarget(target);
        pfBean.addAdvisor(new DefaultPointcutAdvisor(pointcut, new UppercaseAdvice()));
        Hello proxiedHello = (Hello) pfBean.getObject();

        if (adviced) {
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("HELLO TOBY");
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("HI TOBY");
            assertThat(proxiedHello.sayThankYou(("Toby"))).isEqualTo("Thank You Toby");
        } else {
            assertThat(proxiedHello.sayHello("Toby")).isEqualTo("Hello Toby");
            assertThat(proxiedHello.sayHi("Toby")).isEqualTo("Hi Toby");
            assertThat(proxiedHello.sayThankYou(("Toby"))).isEqualTo("Thank You Toby");
        }
    }

}
