package com.shouxin.alerm;


import com.shouxin.alerm.Datasource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

//@EnableAutoConfiguration
@SpringBootApplication
//@EnableAsync
//@ServletComponentScan
@EnableScheduling
@EnableTransactionManagement
@Import({DynamicDataSourceRegister.class}) // 注册动态多数据源
//@ComponentScan(basePackages="com.shouxin.alerm")
public class ShouxinAlermTransformApplication {

	public static void main(String[] args) {
		SpringApplication.run(ShouxinAlermTransformApplication.class, args);
	}
}
