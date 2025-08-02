package ru.practicum.eventservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDiscoveryClient
@ConfigurationPropertiesScan
@EnableFeignClients(basePackages = {"ru.practicum.client", "ru.practicum.interaction.feign"})
@ComponentScan(basePackages = {"ru.practicum.client", "ru.practicum.eventservice"})
public class EventServiceApp {
    public static void main(String[] args) {
        SpringApplication.run(EventServiceApp.class, args);
    }
}
