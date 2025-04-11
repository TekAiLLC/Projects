/*
 * Copyright (c) 2025 Tek-AI LLC
 * All rights reserved.
 *
 * Created on 11-Apr-2025.
 */

package com.tekai.ck.orderservice;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.core.env.Environment;

@SpringBootApplication
public class CloudKitchenOrderService
{

    // This is the start of the Order Service
    public static void main(String[] args)
    {
        SpringApplication.run(CloudKitchenOrderService.class, args);
    }

    // For checking properties are loaded properly.
    @Bean
    CommandLineRunner showProps(Environment env)
    {
        return args ->
        {
            System.out.println("Strated application : " + env.getProperty("spring.application.name"));
            System.out.println("Spring loaded URL: " + env.getProperty("spring.datasource.url"));
            System.out.println("Listening Port: " + env.getProperty("server.port"));
            // TODO : Add other required properties as well.
        };
    }
}
