package com.empresa.apipagamento.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.domain.AuditorAware;
import org.springframework.context.annotation.Bean;
import java.util.Optional;

@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class JpaConfig {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // TODO: Implement real user authentication and return the actual username
        return () -> Optional.of("system");
    }
}