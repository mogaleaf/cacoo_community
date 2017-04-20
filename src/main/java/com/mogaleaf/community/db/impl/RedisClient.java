package com.mogaleaf.community.db.impl;

import com.google.gson.Gson;
import com.mogaleaf.auth.UserToken;
import com.mogaleaf.community.db.DatabaseService;
import com.mogaleaf.community.model.Diagram;
import com.mogaleaf.helper.PropertieHelper;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class RedisClient implements DatabaseService {

    protected Jedis client;
    protected static String LOGGED_PATTERN = ".logged";
    protected static String CUR_LOG_PATTERN = ".currLog";
    protected static String DIAGRAM_RATE_KEY = "diagrams.rate";
    protected static String DIAGRAM_NB_RATE_KEY = "diagrams.nb.rate";
    protected static String DIAGRAM_RECENT_KEY = "diagrams.recent";
    protected static String DIAGRAM_KEY = "diagrams";
    private Gson gson = new Gson();
    private String sha1AddDiagram;
    private String sha1RateDiagram;

    @PostConstruct
    public void start() {
        client = new Jedis(PropertieHelper.appProperties.getProperty("redis.ip"), Integer.valueOf(PropertieHelper.appProperties.getProperty("redis.port")));
        sha1AddDiagram = client.scriptLoad(RedisScript.ADD_DIAGRAM);
        sha1RateDiagram = client.scriptLoad(RedisScript.RATE_DIAGRAM);
    }

    /**
     * Register credential in Redis.
     */
    @Override
    public void registerCredential(String name, UserToken userToken) {
        String key;
        int time;
        if (userToken.tempToken) {
            key = name + CUR_LOG_PATTERN;
            time = 60 * 2;
        } else {
            key = name + LOGGED_PATTERN;
            time = 60 * 60 * 24 * 2;
        }
        client.set(key, gson.toJson(userToken));
        client.expire(key, time);

    }

    /**
     * Retrieve UserToken from redis.
     */
    @Override
    public UserToken retrieveCredential(String name) {
        String tokenString = null;
        if (client.exists(name + LOGGED_PATTERN)) {
            tokenString = client.get(name + LOGGED_PATTERN);
        } else if (client.exists(name + CUR_LOG_PATTERN)) {
            tokenString = client.get(name + CUR_LOG_PATTERN);
        }
        if (tokenString != null) {
            return gson.fromJson(tokenString, UserToken.class);
        }
        return null;
    }

    @Override
    public boolean logUser(String name) {
        return client.exists(name + LOGGED_PATTERN);
    }

    @Override
    public boolean curLogUser(String name) {
        return client.exists(name + CUR_LOG_PATTERN);
    }

    /**
     * Use script to add a diagram so its thread safe.
     */
    @Override
    public void addDiagrams(List<Diagram> retrieveDiags) {
        retrieveDiags.forEach(retrieveDiag -> {
            if (!client.hexists(DIAGRAM_KEY, retrieveDiag.id)) {
                client.evalsha(sha1AddDiagram, 4, DIAGRAM_KEY, DIAGRAM_RATE_KEY, DIAGRAM_NB_RATE_KEY, DIAGRAM_RECENT_KEY, retrieveDiag.id, gson.toJson(retrieveDiag));
            }
        });
    }

    @Override
    public List<Diagram> retrieveMostPopular(int i) {
        Set<String> zrevrange = client.zrevrange(DIAGRAM_RATE_KEY, 0, i);
        return retriveDiagsFromJson(zrevrange);
    }

    @Override
    public List<Diagram> retrieveMostRecent(int i) {
        List<String> lrange = client.lrange(DIAGRAM_RECENT_KEY, 0, i);
        return retriveDiagsFromJson(lrange);
    }

    /**
     * Use script to rate a diagram so its thread safe.
     *
     * @param diagId
     * @param score
     */
    @Override
    public void rate(String diagId, int score) {
        client.evalsha(sha1RateDiagram, 2, DIAGRAM_NB_RATE_KEY, DIAGRAM_RATE_KEY, diagId, String.valueOf(score));
    }

    @Override
    public boolean exist(String diagId) {
        return client.hexists(DIAGRAM_KEY, diagId);
    }

    private List<Diagram> retriveDiagsFromJson(Collection<String> zrevrange) {
        return zrevrange.stream().map(this::retrieveDiag).collect(Collectors.toList());
    }

    private Diagram retrieveDiag(String diagId) {
        Diagram diagram = gson.fromJson(client.hget(DIAGRAM_KEY, diagId), Diagram.class);
        diagram.rate = client.zscore(DIAGRAM_RATE_KEY, diagId);
        diagram.numberOfRate = Integer.valueOf(client.hget(DIAGRAM_NB_RATE_KEY, diagId));
        return diagram;
    }

}
