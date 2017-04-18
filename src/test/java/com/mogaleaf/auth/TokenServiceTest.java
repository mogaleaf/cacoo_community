package com.mogaleaf.auth;

import com.mogaleaf.community.db.DatabaseService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class TokenServiceTest {

    @Mock
    private OAuthService oAuthService;

    @Mock
    private DatabaseService databaseService;

    @InjectMocks
    private TokenService testInstance = new TokenService();

    @Before
    public void setup() throws IOException {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void getAuthentificationUrlTest() throws IOException {
        UserToken fakeUserToken = new UserToken();
        Mockito.when(oAuthService.retrieveTempToken("https://cacoo.com/oauth/request_token","http://localhost:8080/user/callback?email=test")).thenReturn(fakeUserToken);
        Mockito.when(oAuthService.retrieveAuthorizeUrl("https://cacoo.com/oauth/authorize", fakeUserToken)).thenReturn("http://mock/auth/test");
        String test = testInstance.getAuthentificationUrl("test","http://localhost:8080/user/callback?email=test");
        assertThat(test).isEqualTo("http://mock/auth/test");
        Mockito.verify(databaseService).registerCredential("test", fakeUserToken);
    }

    @Test
    public void requestAndRegisterTokenTest() throws IOException {
        UserToken fakeUserToken = new UserToken();
        Mockito.when(databaseService.retrieveCredential("test")).thenReturn(fakeUserToken);
        UserToken secondFakeUserToken = new UserToken();
        Mockito.when(oAuthService.retrieveToken("https://cacoo.com/oauth/access_token",fakeUserToken,"verif")).thenReturn(secondFakeUserToken);
        testInstance.requestAndRegisterToken("test", "verif");
        Mockito.verify(databaseService).retrieveCredential("test");
        Mockito.verify(databaseService).registerCredential("test", secondFakeUserToken);
    }
}
