package kr.pe.junho85.spring.ch06;

public class HelloUppercase implements Hello {
    Hello hello; // 위임할 타깃 오브젝. 여기서는 타깃 클래스의 오브젝트인 것은 알지만
                // 다른 프록시를 추가할 수도 있으므로 인터페이스로 접근한다

    public HelloUppercase(Hello hello) { this.hello = hello; };

    // 위임과 부가기능 적용
    public String sayHello(String name) {return hello.sayHello(name).toUpperCase();}
    public String sayHi(String name) {return hello.sayHi(name).toUpperCase();}
    public String sayThankYou(String name) {return hello.sayThankYou(name).toUpperCase();}

    // 문제점: 인터페이스의 모든 메소드 구현해 위임 대문자 바꾸는 기능 중복
}
