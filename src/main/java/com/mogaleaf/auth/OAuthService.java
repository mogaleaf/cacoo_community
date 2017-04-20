package com.mogaleaf.auth;

import java.io.IOException;

public interface OAuthService {

    /**
     * Retrieve temp token from authorize url.
     *
     * @param urlRequestTempToken
     * @param callBack
     * @return
     * @throws IOException
     */
    public UserToken retrieveTempToken(String urlRequestTempToken, String callBack) throws IOException;

    /**
     * Retrieve Full token from temp token.
     *
     * @param urlAccessToken
     * @param tempToken
     * @param verify
     * @return
     * @throws IOException
     */
    public UserToken retrieveToken(String urlAccessToken, UserToken tempToken, String verify) throws IOException;

    /**
     * Retrieve authorize url.
     *
     * @param authUrl
     * @param tempToken
     * @return
     */
    public String retrieveAuthorizeUrl(String authUrl, UserToken tempToken);

}
