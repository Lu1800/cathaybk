package com.example;


import com.example.entity.Coin;
import com.example.repository.CoinRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


@SpringBootApplication
//@EntityScan("com.example.entity")
//@EnableJpaRepositories("com.example.repository")
//@Configuration
public class DemoApplication {

	@Bean
	InitializingBean saveData(CoinRepository repo){
		return ()->{
			repo.save(new Coin("USD","美金"));
			repo.save(new Coin("GBP","英鎊"));
			repo.save(new Coin("EUR","歐元"));

		};
	}

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}



}
