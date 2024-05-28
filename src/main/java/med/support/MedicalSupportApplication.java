package med.support;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import java.nio.file.Path;
import java.nio.file.Paths;

@SpringBootApplication
public class MedicalSupportApplication
{
    public static void main(String[] args) {
        SpringApplication.run(MedicalSupportApplication.class, args);
        Path currentRelativePath = Paths.get("");
        String s = currentRelativePath.toAbsolutePath().toString();
        System.out.println("Current absolute path is: " + s);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }


}
