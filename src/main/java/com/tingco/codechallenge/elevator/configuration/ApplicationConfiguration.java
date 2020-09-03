package com.tingco.codechallenge.elevator.configuration;

import com.google.common.eventbus.AsyncEventBus;
import com.google.common.eventbus.EventBus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

@Configuration
public class ApplicationConfiguration {

    @Value("${com.tingco.elevator.numberofelevators}")
    private int numberOfElevators;

    /**
     * Create a default thread pool for your convenience.
     *
     * @return Executor thread pool
     */
    @Bean(destroyMethod = "shutdown")
    public ScheduledExecutorService taskExecutor() {
        return Executors.newScheduledThreadPool(numberOfElevators);
    }

    /**
     * Create an event bus for your convenience.
     *
     * @return EventBus for async task execution
     */
    @Bean
    public EventBus eventBus() {
        return new AsyncEventBus(Executors.newCachedThreadPool());
    }
}
