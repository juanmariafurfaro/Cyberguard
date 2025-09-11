package com.furfaro.cyberguard;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class CyberguardApplication {

	public static void main(String[] args) {
		SpringApplication.run(CyberguardApplication.class, args);
	}

}






