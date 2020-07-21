package com.example.demospringboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class DemospringbootApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemospringbootApplication.class, args);
    }

}
