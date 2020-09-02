package com.tingco.codechallenge.elevator.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * Preconfigured Spring Application boot class.
 *
 */
@SpringBootApplication
@ComponentScan(basePackages = {"com.tingco.codechallenge.elevator"})
public class ElevatorApplication {

    /**
     * Start method that will be invoked when starting the Spring context.
     *
     * @param args Not in use
     */
    public static void main(final String[] args) {
        SpringApplication.run(ElevatorApplication.class, args);
    }
}
