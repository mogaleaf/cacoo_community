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
        fakeUserToken.token = "token";
        Mockito.when(oAuthService.retrieveTempToken("https://cacoo.com/oauth/request_token","http://localhost:8080/signup/callback")).thenReturn(fakeUserToken);
        Mockito.when(oAuthService.retrieveAuthorizeUrl("https://cacoo.com/oauth/authorize", fakeUserToken)).thenReturn("http://mock/auth/test");
        String test = testInstance.getAuthentificationUrl("http://localhost:8080/signup/callback");
        assertThat(test).isEqualTo("http://mock/auth/test");
        Mockito.verify(databaseService).registerCredential(fakeUserToken.token , fakeUserToken);
    }

    @Test
    public void requestAndRegisterTokenTest() throws IOException {
        UserToken fakeUserToken = new UserToken();
        Mockito.when(databaseService.retrieveCredential(fakeUserToken.token)).thenReturn(fakeUserToken);
        UserToken secondFakeUserToken = new UserToken();
        Mockito.when(oAuthService.retrieveToken("https://cacoo.com/oauth/access_token",fakeUserToken,"verif")).thenReturn(secondFakeUserToken);
        String id = testInstance.requestAndRegisterToken(fakeUserToken.token, "verif");
        Mockito.verify(databaseService).retrieveCredential(fakeUserToken.token );
        Mockito.verify(databaseService).registerCredential(id, secondFakeUserToken);
    }
}
