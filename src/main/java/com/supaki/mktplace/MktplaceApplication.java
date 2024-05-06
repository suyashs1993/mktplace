package com.supaki.mktplace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
@EnableJpaAuditing
public class MktplaceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MktplaceApplication.class, args);
	}

}
