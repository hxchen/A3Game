package com.a3fun;


import com.a3fun.pudding.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class PuddingApp {

    public static void main(String[] args) {
        ConfigurableApplicationContext applicationContext = SpringApplication.run(PuddingApp.class, args);
        Application.setApplicationContext(applicationContext);
    }
}
