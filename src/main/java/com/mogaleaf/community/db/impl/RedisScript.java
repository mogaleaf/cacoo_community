package com.mogaleaf.community.db.impl;

public class RedisScript {
    /**
     * Insert diagram in general diag key.
     * Insert diagId in rating zadd
     * Insert diagId in nbrating set
     * Insert diagId in linkedlist.
     */
    public static String ADD_DIAGRAM = "redis.call('hset',KEYS[1],ARGV[1],ARGV[2])\n"
            + " redis.call('zadd',KEYS[2],0,ARGV[1])\n"
            + "redis.call('lpush',KEYS[4],ARGV[1])\n"
            + "return redis.call('hset',KEYS[3],ARGV[1],'0')";


    /**
     * Retrieve rate value with the diagId.
     * Retrieve nb rating people from diagId.
     * Calcul the average.
     * Update both keys.
     */
    public static String RATE_DIAGRAM = "local nbRateValue = redis.call('hget', KEYS[1],ARGV[1])\n" +
            "local rateValue = redis.call('zscore', KEYS[2],ARGV[1])\n" +
            "local newRateValue = (rateValue*nbRateValue + ARGV[2])/(nbRateValue+1)\n" +
            "redis.call('zadd', KEYS[2],newRateValue,ARGV[1])\n" +
            "return redis.call('hset', KEYS[1],ARGV[1],nbRateValue+1)";


}
