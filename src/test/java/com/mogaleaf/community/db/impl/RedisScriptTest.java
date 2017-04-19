package com.mogaleaf.community.db.impl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class RedisScriptTest {
    redis.embedded.RedisServer redisServer = null;
    int port = 6381;

    @Before
    public void setup() {

        try {
            redisServer = new redis.embedded.RedisServer(port);
            redisServer.start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @After
    public void tearDown() {
        redisServer.stop();
    }

    @Test
    public void addDiagramTest() {
        Jedis client = new Jedis("localhost", port);
        client.flushAll();
        String sha1AddDiagram = client.scriptLoad(RedisScript.ADD_DIAGRAM);
        List<String> keys = new ArrayList<>();
        keys.add(RedisClient.DIAGRAM_KEY);
        keys.add(RedisClient.DIAGRAM_RATE_KEY);
        keys.add(RedisClient.DIAGRAM_NB_RATE_KEY);
        keys.add(RedisClient.DIAGRAM_RECENT_KEY);
        List<String> args = new ArrayList<>();
        args.add("id");
        args.add("{id:id}");
        client.evalsha(sha1AddDiagram, keys, args);
        assertThat(client.hget(RedisClient.DIAGRAM_KEY, "id")).isEqualTo("{id:id}");
        assertThat(client.lpop(RedisClient.DIAGRAM_RECENT_KEY)).isEqualTo("id");
        assertThat(client.zrange(RedisClient.DIAGRAM_RATE_KEY, 0, 0)).hasSize(1).containsExactly("id");
        client.close();
    }

    @Test
    public void rateDiagramTest(){
        Jedis client = new Jedis("localhost", port);
        client.flushAll();
        String sha1AddDiagram = client.scriptLoad(RedisScript.RATE_DIAGRAM);

        client.hset(RedisClient.DIAGRAM_NB_RATE_KEY,"id","0");
        client.zadd(RedisClient.DIAGRAM_RATE_KEY,0,"id");

        List<String> keys = new ArrayList<>();
        keys.add(RedisClient.DIAGRAM_NB_RATE_KEY);
        keys.add(RedisClient.DIAGRAM_RATE_KEY);
        List<String> args = new ArrayList<>();
        args.add("id");
        args.add("4");
        client.evalsha(sha1AddDiagram, keys, args);
        assertThat(client.zscore(RedisClient.DIAGRAM_RATE_KEY,"id")).isEqualTo(4);
        assertThat(client.hget(RedisClient.DIAGRAM_NB_RATE_KEY,"id")).isEqualTo("1");
    }
}
