package ch.uzh.ifi.seal.soprafs19.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = "ch.uzh.ifi.seal.soprafs19")
@EnableJpaRepositories(basePackages = "ch.uzh.ifi.seal.soprafs19.repository")
@EntityScan("ch.uzh.ifi.seal.soprafs19.entity")
public class AppConfig {
    // You can define additional configurations here if needed
}