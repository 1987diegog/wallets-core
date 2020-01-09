package uy.com.demente.ideas.wallets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * @author 1987diegog
 */
@SpringBootApplication()
//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
@EnableJpaAuditing
public class WalletsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletsApplication.class, args);
	}
}

