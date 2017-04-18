package com.mogaleaf.community.db.impl;

import com.google.gson.Gson;
import com.mogaleaf.auth.UserToken;
import com.mogaleaf.community.db.DatabaseService;
import com.mogaleaf.community.model.Diagram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Service
public class RedisClient implements DatabaseService {

    @Autowired
    private com.mogaleaf.community.db.impl.RedisServer server;



    protected Jedis client;
    protected static String LOGGED_PATTERN = ".logged";
    protected static String CUR_LOG_PATTERN = ".currLog";
    protected static String DIAGRAM_RATE_KEY = "diagrams.rate";
    protected static String DIAGRAM_RECENT_KEY = "diagrams.recent";
    protected static String DIAGRAM_KEY = "diagrams";
    private Gson gson = new Gson();

    @PostConstruct
    public void start() {
        client = new Jedis("localhost", 6379);
    }

    @Override
    public void registerCredential(String email, UserToken userToken) {
        String key;
        int time;
        if (userToken.tempToken) {
            key = email + CUR_LOG_PATTERN;
            time = 60 * 2;
        } else {
            key = email + LOGGED_PATTERN;
            time = 60 * 60 * 24 * 2;
        }
        client.set(key, gson.toJson(userToken));
        client.expire(key, time);

    }

    @Override
    public UserToken retrieveCredential(String email) {
        String tokenString = null;
        if (client.exists(email + LOGGED_PATTERN)) {
            tokenString = client.get(email + LOGGED_PATTERN);
        } else if (client.exists(email + CUR_LOG_PATTERN)) {
            tokenString = client.get(email + CUR_LOG_PATTERN);
        }
        if (tokenString != null) {
            return gson.fromJson(tokenString, UserToken.class);
        }
        return null;
    }

    @Override
    public boolean logUser(String email) {
        return client.exists(email + LOGGED_PATTERN);
    }

    @Override
    public boolean curLogUser(String email) {
        return client.exists(email + CUR_LOG_PATTERN);
    }

    @Override
    public void addDiagrams(List<Diagram> retrieveDiags) {
        for (Diagram retrieveDiag : retrieveDiags) {
            client.hset(DIAGRAM_KEY, retrieveDiag.id, gson.toJson(retrieveDiag));
            client.zadd(DIAGRAM_RATE_KEY, 0, retrieveDiag.id);
            client.lpush(DIAGRAM_RECENT_KEY, retrieveDiag.id);
        }
    }

    @Override
    public List<Diagram> retrieveMostPopular(int i) {
        Set<String> zrevrange = client.zrevrange(DIAGRAM_RATE_KEY, 0, i);
        List<Diagram> returnList = retriveDiagsFromJson(zrevrange);
        return returnList;
    }

    @Override
    public List<Diagram> retrieveMostRecent(int i) {
        List<String> lrange = client.lrange(DIAGRAM_RECENT_KEY, 0, i);
        List<Diagram> returnList = retriveDiagsFromJson(lrange);
        return returnList;
    }

    @Override
    public Diagram retrieve(String diagId) {
        return gson.fromJson(client.hget(DIAGRAM_KEY, diagId), Diagram.class);
    }

    @Override
    public void update(Diagram diag) {
        client.zadd(DIAGRAM_RATE_KEY, diag.rate, diag.id);
        client.hset(DIAGRAM_KEY, diag.id, gson.toJson(diag));
    }

    private List<Diagram> retriveDiagsFromJson(Collection<String> zrevrange) {
        List<Diagram> returnList = new ArrayList<>();
        for (String anId : zrevrange) {
            returnList.add(gson.fromJson(client.hget(DIAGRAM_KEY, anId), Diagram.class));
        }
        return returnList;
    }
}
