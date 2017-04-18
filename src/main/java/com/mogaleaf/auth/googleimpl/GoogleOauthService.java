package com.mogaleaf.auth.googleimpl;

import com.google.api.client.auth.oauth.OAuthAuthorizeTemporaryTokenUrl;
import com.google.api.client.auth.oauth.OAuthCredentialsResponse;
import com.google.api.client.auth.oauth.OAuthGetAccessToken;
import com.google.api.client.auth.oauth.OAuthGetTemporaryToken;
import com.mogaleaf.auth.OAuthService;
import com.mogaleaf.auth.Transport;
import com.mogaleaf.auth.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class GoogleOauthService implements OAuthService {

    @Autowired
    private Transport transport;

    @Override
    public UserToken retrieveTempToken(String urlRequestTempToken,String callBack) throws IOException {
        OAuthBuilder oAuthBuilder = new OAuthBuilder(transport);
        OAuthGetTemporaryToken getToken = new OAuthGetTemporaryToken("https://cacoo.com/oauth/request_token");
        getToken.callback = callBack;
        getToken.transport = oAuthBuilder.netHttpTransport;
        getToken.signer = oAuthBuilder.oAuthHmacSigner;
        getToken.consumerKey = oAuthBuilder.consumerKey;
        OAuthCredentialsResponse execute = getToken.execute();
        return buildUserToken(execute.token,execute.tokenSecret,true);
    }

    @Override
    public UserToken retrieveToken(String urlAccessToken, UserToken tempToken,String verify) throws IOException {
        OAuthBuilder oAuthBuilder = new OAuthBuilder(transport);
        OAuthGetAccessToken accessToken = new OAuthGetAccessToken(urlAccessToken);
        accessToken.temporaryToken = tempToken.token;
        accessToken.verifier = verify;
        accessToken.transport = oAuthBuilder.netHttpTransport;
        accessToken.signer = oAuthBuilder.oAuthHmacSigner;
        accessToken.consumerKey = oAuthBuilder.consumerKey;

        oAuthBuilder.oAuthHmacSigner.tokenSharedSecret = tempToken.tokenSecret;
        OAuthCredentialsResponse response = accessToken.execute();

        return buildUserToken(response.token,response.tokenSecret,false);
    }

    @Override
    public String retrieveAuthorizeUrl(String authUrl, UserToken tempToken) {
        OAuthAuthorizeTemporaryTokenUrl urlAuth = new OAuthAuthorizeTemporaryTokenUrl(authUrl);
        urlAuth.temporaryToken = tempToken.token;
        return urlAuth.toURL().toString();
    }

    private UserToken buildUserToken(String token,String tokenSecret,boolean temp) {
        UserToken userToken = new UserToken();
        userToken.token = token;
        userToken.tokenSecret = tokenSecret;
        userToken.tempToken = temp;
        return userToken;
    }
}
