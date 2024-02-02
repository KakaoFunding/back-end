package org.kakaoshare.backend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackEndApplication {
	@Autowired
	private TestComponent testComponent;

	public static void main(String[] args) {
		SpringApplication.run(BackEndApplication.class, args);
	}

}
