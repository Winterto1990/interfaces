package com.inspur.monitorTranslate;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class MonitorTranslateApplication {

	public static void main(String[] args) {
		SpringApplication.run(MonitorTranslateApplication.class, args);
	}
}
