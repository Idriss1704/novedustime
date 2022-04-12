package bf.novedus.novedustime;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NovedusTime {

	//comment
	public static void main(String[] args) {
		SpringApplication.run(NovedusTime.class, args);
	}
	

}
