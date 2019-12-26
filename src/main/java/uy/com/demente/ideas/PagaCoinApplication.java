package uy.com.demente.ideas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })

/**
 * @author 1987diegog
 */
@SpringBootApplication()
public class PagaCoinApplication {

	public static void main(String[] args) {
		SpringApplication.run(PagaCoinApplication.class, args);
	}
}
