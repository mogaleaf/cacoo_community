package com.mogaleaf.community.api;

import com.mogaleaf.auth.TokenService;
import com.mogaleaf.community.api.service.DiagramsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RequestsSignupController {

    @Autowired
    private TokenService tokenService;

    static Logger logger = LoggerFactory.getLogger(RequestsSignupController.class);


    @RequestMapping("/signup")
    public void newUser(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("A user start authentification");
        try {
            String baseUrl = String.format("%s://%s:%d/signup/callback", request.getScheme(), request.getServerName(), request.getServerPort());
            response.sendRedirect(tokenService.getAuthentificationUrl(baseUrl));
        } catch (IOException e) {
            logger.error("Problem with new user ", e);
        }
    }

    @RequestMapping("/signup/callback")
    public String callBack(HttpServletRequest request, HttpServletResponse response,@RequestParam("oauth_token") String oauth_token, @RequestParam("oauth_verifier") String verifier) {
        logger.debug("A user callback");
        try {
            if (tokenService.currentLoggingUser(oauth_token)) {
                String sessionId = tokenService.requestAndRegisterToken(oauth_token, verifier);
                return sessionId;
            } else {
                String baseUrl = String.format("%s://%s:%d/signup/new", request.getScheme(), request.getServerName(), request.getServerPort());
                response.sendRedirect(tokenService.getAuthentificationUrl(baseUrl));
            }
        } catch (IOException e) {
            logger.error("Problem with callBack user ", e);
        }
        return null;
    }
}
