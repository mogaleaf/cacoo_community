package com.mogaleaf.auth;

import com.mogaleaf.community.db.DatabaseService;
import com.mogaleaf.helper.PropertieHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class TokenService {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private DatabaseService databaseService;

    public boolean loggedUser(String email){
        return  !(email == null || email.isEmpty()) && databaseService.logUser(email);
    }

    public boolean currentLoggingUser(String email){
        return !(email == null || email.isEmpty()) && databaseService.curLogUser(email);
    }

    public String getAuthentificationUrl(String email,String callBackUrl) throws IOException {
        UserToken userToken = oAuthService.retrieveTempToken(PropertieHelper.urlProperties.getProperty("request"),callBackUrl);
        databaseService.registerCredential(email, userToken);
        return oAuthService.retrieveAuthorizeUrl(PropertieHelper.urlProperties.getProperty("authorize"), userToken);
    }

    public void requestAndRegisterToken(String email, String verif) throws IOException {
        UserToken userToken = databaseService.retrieveCredential(email);
        userToken = oAuthService.retrieveToken(PropertieHelper.urlProperties.getProperty("access"),userToken,verif);
        databaseService.registerCredential(email, userToken);
    }

}
