package com;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.ComponentScan;

/**
 * Created by xuds on 2017/10/20.
 */
//@EnableEurekaClient
//@EnableConfigServer
@ServletComponentScan
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = "com.appmonitor")
public class MonitorApplication {

    public static void main(String[] args){
        SpringApplication.run(MonitorApplication.class,args);
    }
}
