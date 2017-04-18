package com.mogaleaf.auth;

import java.io.IOException;

public interface OAuthService {

    public UserToken retrieveTempToken(String urlRequestTempToken,String callBack) throws IOException;
    public UserToken retrieveToken(String urlAccessToken,UserToken tempToken,String verify) throws IOException;
    public String retrieveAuthorizeUrl(String authUrl,UserToken tempToken);

}
