package dev.cmedina.test.api.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import dev.cmedina.test.api.domain.User;
import dev.cmedina.test.api.repository.UserRepository;

@Configuration
@Profile("local")
public class LocalConfig {

	@Autowired
	private UserRepository repository;
	
	@Bean
	public void startDB() {
		
		User u1 = new User(null, "Cristian", "cristian@gmail.com", "123");
		User u2 = new User(null, "Mariana", "mariana@outlook.com", "123");
		
		repository.saveAll(List.of(u1, u2));
	}
	
}
