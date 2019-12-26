package uy.com.demente.ideas.wallets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

/**
 * @author 1987diegog
 */
@SpringBootApplication()
public class WalletsApplication {

	public static void main(String[] args) {
		SpringApplication.run(WalletsApplication.class, args);
	}
}
