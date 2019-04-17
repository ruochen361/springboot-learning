package com.springboot.learn.elasticjob;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.springboot.learn.elasticjob")
public class SpringbootElasticjobApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootElasticjobApplication.class, args);
	}

}
