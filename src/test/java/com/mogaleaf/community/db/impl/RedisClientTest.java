package com.mogaleaf.community.db.impl;

import com.google.gson.Gson;
import com.mogaleaf.auth.UserToken;
import com.mogaleaf.community.model.Diagram;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;


public class RedisClientTest {

    @Mock
    Jedis client;


    private RedisClient instance = new RedisClient();
    Gson parser = new Gson();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        instance.client = client;
    }

    @Test
    public void registerCredentialTempTest() {
        UserToken falseToken = new UserToken();
        falseToken.tempToken = true;
        falseToken.token = "token";
        falseToken.tokenSecret = "secret";
        instance.registerCredential("email", falseToken);
        Mockito.verify(client).set("email.currLog", parser.toJson(falseToken));
        Mockito.verify(client).expire("email.currLog", 60 * 2);
    }

    @Test
    public void registerCredentialTest() {
        UserToken falseToken = new UserToken();
        falseToken.tempToken = false;
        falseToken.token = "token";
        falseToken.tokenSecret = "secret";
        instance.registerCredential("email", falseToken);
        Mockito.verify(client).set("email.logged", parser.toJson(falseToken));
        Mockito.verify(client).expire("email.logged", 60 * 60 * 24 * 2);
    }

    @Test
    public void retrieveCredentialcurTest() {
        UserToken falseToken = new UserToken();
        Mockito.when(client.exists("email.currLog")).thenReturn(true);
        Mockito.when(client.exists("email.logged")).thenReturn(false);
        Mockito.when(client.get("email.currLog")).thenReturn(parser.toJson(falseToken));
        UserToken email = instance.retrieveCredential("email");
        assertThat(parser.toJson(falseToken)).isEqualTo(parser.toJson(email));
    }

    @Test
    public void retrieveCredentialLoggedTest() {
        UserToken falseToken = new UserToken();
        Mockito.when(client.exists("email.currLog")).thenReturn(false);
        Mockito.when(client.exists("email.logged")).thenReturn(true);
        Mockito.when(client.get("email.logged")).thenReturn(parser.toJson(falseToken));
        UserToken email = instance.retrieveCredential("email");
        assertThat(parser.toJson(falseToken)).isEqualTo(parser.toJson(email));
    }

    @Test
    public void addDiagramsTest() {
        List<Diagram> retrieveDiags = new ArrayList<>();

        Diagram d1 = new Diagram();
        d1.id = "1";
        Diagram d2 = new Diagram();
        d2.id = "2";

        retrieveDiags.add(d1);
        retrieveDiags.add(d2);
        instance.addDiagrams(retrieveDiags);
        Mockito.verify(client).evalsha(null, 4, RedisClient.DIAGRAM_KEY, RedisClient.DIAGRAM_RATE_KEY, RedisClient.DIAGRAM_NB_RATE_KEY, RedisClient.DIAGRAM_RECENT_KEY, d1.id, parser.toJson(d1));
        Mockito.verify(client).evalsha(null, 4, RedisClient.DIAGRAM_KEY, RedisClient.DIAGRAM_RATE_KEY, RedisClient.DIAGRAM_NB_RATE_KEY, RedisClient.DIAGRAM_RECENT_KEY, d2.id, parser.toJson(d2));


    }

    @Test
    public void retrieveMostPopularTest(){
        Set<String> retrieveDiags = new HashSet<>();
        retrieveDiags.add("1");
        retrieveDiags.add("2");

        Diagram d1 = new Diagram();
        d1.id = "1";
        Diagram d2 = new Diagram();
        d2.id = "2";

        Mockito.when(client.zrevrange(RedisClient.DIAGRAM_RATE_KEY,0,2)).thenReturn(retrieveDiags);
        Mockito.when(client.hget(RedisClient.DIAGRAM_KEY, "1")).thenReturn(parser.toJson(d1));
        Mockito.when(client.hget(RedisClient.DIAGRAM_KEY, "2")).thenReturn(parser.toJson(d2));

        List<Diagram> diagrams = instance.retrieveMostPopular(2);

        assertThat(diagrams).hasSize(2);
        assertThat(diagrams.get(0).id).isEqualTo("1");
        assertThat(diagrams.get(1).id).isEqualTo("2");
    }

    @Test
    public void retrieveMostRecentTest(){
        List<String> retrieveDiags = new ArrayList<>();
        retrieveDiags.add("1");
        retrieveDiags.add("2");

        Diagram d1 = new Diagram();
        d1.id = "1";
        Diagram d2 = new Diagram();
        d2.id = "2";

        Mockito.when(client.lrange(RedisClient.DIAGRAM_RECENT_KEY, 0, 2)).thenReturn(retrieveDiags);
        Mockito.when(client.hget(RedisClient.DIAGRAM_KEY, "1")).thenReturn(parser.toJson(d1));
        Mockito.when(client.hget(RedisClient.DIAGRAM_KEY, "2")).thenReturn(parser.toJson(d2));

        List<Diagram> diagrams = instance.retrieveMostRecent(2);

        assertThat(diagrams).hasSize(2);
        assertThat(diagrams.get(0).id).isEqualTo("1");
        assertThat(diagrams.get(1).id).isEqualTo("2");
    }


}
