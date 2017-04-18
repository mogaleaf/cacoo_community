package com.mogaleaf.auth;

import com.mogaleaf.community.db.DatabaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Properties;

@Service
public class TokenService {

    @Autowired
    private OAuthService oAuthService;

    @Autowired
    private DatabaseService databaseService;

    static Logger logger = LoggerFactory.getLogger(TokenService.class);

    static Properties properties = new Properties();
    static{

        try {
            properties.load(TokenService.class.getClassLoader().getResourceAsStream("url.properties"));
        }catch(Exception e){
            logger.error("Load properties problem ",e );
        }
    }

    public boolean loggedUser(String email){
        if(email == null || email.isEmpty()){
            return false;
        }
        return databaseService.logUser(email);
    }

    public boolean currentLoggingUser(String email){
        if(email == null || email.isEmpty()){
            return false;
        }
        return databaseService.curLogUser(email);
    }

    public String getAuthentificationUrl(String email,String callBackUrl) throws IOException {
        UserToken userToken = oAuthService.retrieveTempToken(properties.getProperty("request"),callBackUrl);
        databaseService.registerCredential(email, userToken);
        return oAuthService.retrieveAuthorizeUrl(properties.getProperty("authorize"), userToken);
    }

    public void requestAndRegisterToken(String email, String verif) throws IOException {
        UserToken userToken = databaseService.retrieveCredential(email);
        userToken = oAuthService.retrieveToken(properties.getProperty("access"),userToken,verif);
        databaseService.registerCredential(email, userToken);
    }

}
