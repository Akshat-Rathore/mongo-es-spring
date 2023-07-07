package com.task1.take1;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@SpringBootApplication
@EnableMongoRepositories(basePackages = "com.task1.take1.repository")
@EnableElasticsearchRepositories(basePackages="com.task1.take1.eRepository")
@ComponentScan(basePackages = "com.task1.take1")
//@Enable
public class Take1Application {

	public static void main(String[] args) {
		SpringApplication.run(Take1Application.class, args);
	}

//	@Bean
//	CommandLineRunner runner(UserRepository repository){
//		return args -> {
//			User user = new User("me",List.of("The only one"));
//			repository.insert(user);
//		};
//	}
	@Bean
	public RestTemplate restTemplate(){
		return new RestTemplate();
	}
}
