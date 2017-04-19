package com.mogaleaf;

import com.mogaleaf.community.db.impl.RedisClient;
import com.mogaleaf.community.db.impl.RedisServer;
import com.mogaleaf.community.model.Diagram;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RequestsApiControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    static RedisServer server = new RedisServer();

    @Autowired
    RedisClient client;

    @BeforeClass
    public static void setup(){
        server.start();
    }

    @AfterClass
    public static void tearDown() {
        server.stop();
    }




    @Test
    public void requestPopularTest() {
        Diagram test = new Diagram();
        test.id = "d1";
        test.name = "my first diag";
        test.imageUrl = "http://anurl.com";
        client.addDiagrams(Collections.singletonList(test));

        Map<String,String> rateParams = new HashMap<>();
        rateParams.put("diagId", "d1");
        rateParams.put("score", "5");
        restTemplate.getRestTemplate().getForEntity("/api/rate", Void.class, rateParams);
        ResponseEntity<List> forEntity1 = restTemplate.getRestTemplate().getForEntity("/api/popular", List.class);

        List<Map> body = forEntity1.getBody();
        assertThat(body).hasSize(1);
        Map<String,String> result = body.get(0);
        assertThat(result.get("id")).isEqualTo(test.id);
        assertThat(result.get("name")).isEqualTo(test.name);
        assertThat(result.get("imageUrl")).isEqualTo(test.imageUrl);

    }

    @Test
    public void requestRecentTest() {
        Diagram test = new Diagram();
        test.id = "d1";
        test.name = "my first diag";
        test.imageUrl = "http://anurl.com";
        client.addDiagrams(Collections.singletonList(test));

        Map<String,String> rateParams = new HashMap<>();
        rateParams.put("diagId", "d1");
        rateParams.put("score", "5");
        restTemplate.getRestTemplate().getForEntity("/api/rate", Void.class, rateParams);
        ResponseEntity<List> forEntity1 = restTemplate.getRestTemplate().getForEntity("/api/recent", List.class);

        List<Map> body = forEntity1.getBody();
        assertThat(body).hasSize(1);
        Map<String,String> result = body.get(0);
        assertThat(result.get("id")).isEqualTo(test.id);
        assertThat(result.get("name")).isEqualTo(test.name);
        assertThat(result.get("imageUrl")).isEqualTo(test.imageUrl);

    }

}
