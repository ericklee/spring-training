package com.hkjc.accountsvc.controller;

import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

//@Configuration
public class FunctionDemo {

    @Bean
    Function<Message<Person>, String> process(StreamBridge streamBridge) {
        return v -> {
            System.out.println("Header: " + v.getHeaders());
            System.out.println("Converting message to uppercase: " + v.getPayload().getName());

            streamBridge.send("spring-queue", v.getPayload().getName());

            return v.getPayload().getName().toUpperCase();
        };
    }

    @Bean
    Supplier<String> supply() {
        return () -> "supply";
    }

    @Bean
    Consumer<String> consume() {
        return System.out::println;
    }

    public static class Person {
        String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
