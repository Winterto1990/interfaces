package com.inspur.hksso_union_hy;

import com.inspur.hksso_union_hy.Datasource.DynamicDataSourceRegister;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableAsync
@EnableTransactionManagement
@EnableScheduling
@Import({DynamicDataSourceRegister.class})
public class HkssoUnionHyApplication {

	public static void main(String[] args) {
		SpringApplication.run(HkssoUnionHyApplication.class, args);
	}
}
