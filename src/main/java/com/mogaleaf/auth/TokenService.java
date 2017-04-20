package com.mogaleaf.auth;

import com.mogaleaf.community.db.DatabaseService;
import com.mogaleaf.helper.PropertieHelper;
import org.apache.catalina.SessionIdGenerator;
import org.apache.catalina.util.StandardSessionIdGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TokenService {

    @Autowired
    private OAuthService oAuthService;

    private SessionIdGenerator generator = new StandardSessionIdGenerator();

    @Autowired
    private DatabaseService databaseService;

    public boolean loggedUser(String name) {
        return !(name == null || name.isEmpty()) && databaseService.logUser(name);
    }

    public boolean currentLoggingUser(String email) {
        return !(email == null || email.isEmpty()) && databaseService.curLogUser(email);
    }

    /**
     * Url to authorize token
     *
     * @param callBackUrl
     * @return
     * @throws IOException
     */
    public String getAuthentificationUrl(String callBackUrl) throws IOException {
        UserToken userToken = oAuthService.retrieveTempToken(PropertieHelper.urlProperties.getProperty("request"), callBackUrl);
        databaseService.registerCredential(userToken.token, userToken);
        return oAuthService.retrieveAuthorizeUrl(PropertieHelper.urlProperties.getProperty("authorize"), userToken);
    }

    /**
     * With the tempory access token retrieve a full operational token
     *
     * @param token
     * @param verif
     * @return
     * @throws IOException
     */
    public String requestAndRegisterToken(String token, String verif) throws IOException {
        UserToken userToken = databaseService.retrieveCredential(token);
        userToken = oAuthService.retrieveToken(PropertieHelper.urlProperties.getProperty("access"), userToken, verif);
        String sessionId = generator.generateSessionId();
        databaseService.registerCredential(sessionId, userToken);
        return sessionId;
    }

}
