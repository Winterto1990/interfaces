package com.inspur.collect;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class CollectApplication {

	public static void main(String[] args) {
		SpringApplication.run(CollectApplication.class, args);
	}
}
