package com.tosi.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
/**
 * 스프링 부트는 기본적으로 @SpringBootApplication이 위치한 패키지(com.tosi.user)와 그 하위 패키지만 자동 스캔
 * Config(@Configuration)를 com.tosi.common 같은 다른 패키지로 옮겼다면, 스프링이 자동으로 찾지 못하므로 @ComponentScan을 추가
 */
@ComponentScan(basePackages = "com.tosi")
public class UserApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserApplication.class, args);
	}

}
