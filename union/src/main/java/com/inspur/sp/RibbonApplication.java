package com.inspur.sp;

import com.inspur.sp.datasource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
//import org.springframework.web.client.RestTemplate;

//@EnableDiscoveryClient
//@EnableCircuitBreaker
@EnableAsync
@SpringBootApplication
@ServletComponentScan
@EnableTransactionManagement
@ComponentScan(basePackages="com.inspur.sp.web")
@Import(DynamicDataSourceRegister.class)
@EnableScheduling
public class RibbonApplication {

//	@Bean
//	@LoadBalanced
//	RestTemplate restTemplate() {
//		return new RestTemplate();
//	}

	public static void main(String[] args) {
		SpringApplication.run(RibbonApplication.class, args);
	}

}
