package com.mogaleaf;

import com.mogaleaf.community.db.impl.RedisServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        new RedisServer().start();
        SpringApplication.run(Main.class, args);
    }
}
