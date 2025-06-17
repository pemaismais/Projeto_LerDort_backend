package app.pi_fisio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
		"app.pi_fisio.entity",
		"app.pi_fisio.config"
})
public class PiFisioApplication{

	public static void main(String[] args) {
		SpringApplication.run(PiFisioApplication.class, args);
	}

}
