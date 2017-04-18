package com.mogaleaf.community.db.impl;

import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class RedisServer {
    protected redis.embedded.RedisServer redisServer;

    @PostConstruct
    public void start() {
        try {
            //TODO filepropertie
            redisServer = new redis.embedded.RedisServer(6379);
            redisServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
