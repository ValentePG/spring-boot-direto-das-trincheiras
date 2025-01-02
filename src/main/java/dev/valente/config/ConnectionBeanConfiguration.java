package dev.valente.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Profile;

@Configuration
@RequiredArgsConstructor
public class ConnectionBeanConfiguration {

    private final ConnectionConfigurationProperties connectionConfiguration;

    @Bean
    @Primary
    public Connection connectionMySql() {
        return new Connection(connectionConfiguration.url(),
                connectionConfiguration.username(),
                connectionConfiguration.password());
    }

    @Bean // (name = "connectionMongo")
    @Profile("mongo")
    public Connection connectionMongo() {
        return new Connection(connectionConfiguration.url(),
                connectionConfiguration.username(),
                connectionConfiguration.password());
    }

}
