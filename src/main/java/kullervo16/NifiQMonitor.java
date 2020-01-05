package kullervo16;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class NifiQMonitor {

    public static void main(String[] args) {
        SpringApplication.run(NifiQMonitor.class, args);
    }

}
