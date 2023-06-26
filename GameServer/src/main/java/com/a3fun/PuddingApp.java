package com.a3fun;


import com.a3fun.pudding.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PuddingApp {

    public static void main(String[] args) {
        SpringApplication.run(PuddingApp.class, args);

        Application.initRunTime();

        Application.initWorldScheduler();

        regShutdownHook();

    }

    public static void regShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                System.out.println("优雅的关闭服务, 准备处理收尾工作");
                // do something
                System.out.println("可以安心停服了");
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }, "Shutdown Server"));
    }
}
