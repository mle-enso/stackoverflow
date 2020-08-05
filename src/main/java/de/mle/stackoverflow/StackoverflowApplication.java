package de.mle.stackoverflow;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
@EnableMongoRepositories(basePackages = "de.mle")
@EnableReactiveMongoRepositories(basePackages = "de.mle")
public class StackoverflowApplication {

	public static void main(String[] args) {
		SpringApplication.run(StackoverflowApplication.class, args);
	}
}
