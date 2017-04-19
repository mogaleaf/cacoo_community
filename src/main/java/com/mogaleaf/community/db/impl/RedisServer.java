package com.mogaleaf.community.db.impl;

import com.mogaleaf.helper.PropertieHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class RedisServer {
    protected redis.embedded.RedisServer redisServer;
    static Logger logger = LoggerFactory.getLogger(RedisServer.class);
    public void start() {
        try {
            redisServer = new redis.embedded.RedisServer(Integer.valueOf(PropertieHelper.appProperties.getProperty("redis.port")));
            if(!redisServer.isActive()) {
                redisServer.start();
            }
        } catch (Exception e) {
            logger.error("Probleme starting redis server", e);
        }
    }


    public void stop(){
        redisServer.stop();
    }
}
