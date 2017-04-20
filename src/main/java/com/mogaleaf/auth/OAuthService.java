package com.mogaleaf.auth;

import java.io.IOException;

public interface OAuthService {

    /**
     * Retrieve temp token from authorize url.
     */
    public UserToken retrieveTempToken(String urlRequestTempToken, String callBack) throws IOException;

    /**
     * Retrieve Full token from temp token.
     */
    public UserToken retrieveToken(String urlAccessToken, UserToken tempToken, String verify) throws IOException;

    /**
     * Retrieve authorize url.
     */
    public String retrieveAuthorizeUrl(String authUrl, UserToken tempToken);

}
