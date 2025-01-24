package dev.valente;

import dev.valente.config.BrasilApiConfigurationProperties;
import dev.valente.config.ConnectionConfigurationProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@EnableConfigurationProperties({ConnectionConfigurationProperties.class, BrasilApiConfigurationProperties.class})
public class SpringBootDasTrincheirasApplication {

  public static void main(String[] args) {

    SpringApplication.run(SpringBootDasTrincheirasApplication.class, args);

  }

}
