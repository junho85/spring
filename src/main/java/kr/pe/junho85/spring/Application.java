package kr.pe.junho85.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(basePackages = {"kr.pe.junho85"})
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class);
    }
}
