package com.mogaleaf.community.api;

import com.mogaleaf.auth.TokenService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
public class RequestsSigninController {

    @Autowired
    private TokenService tokenService;

    static Logger logger = LoggerFactory.getLogger(RequestsSigninController.class);


    @RequestMapping("/signin")
    public void newUser(HttpServletRequest request, HttpServletResponse response) {
        logger.debug("A user start authentification");
        try {
            String baseUrl = String.format("%s://%s:%d/signin/callback", request.getScheme(), request.getServerName(), request.getServerPort());
            response.sendRedirect(tokenService.getAuthentificationUrl(baseUrl));
        } catch (IOException e) {
            logger.error("Problem with new user ", e);
        }
    }

    @RequestMapping("/signin/callback")
    public String callBack(HttpServletRequest request, HttpServletResponse response,@RequestParam("oauth_token") String oauth_token, @RequestParam("oauth_verifier") String verifier) {
        logger.debug("A user callback");
        try {
            if (tokenService.currentLoggingUser(oauth_token)) {
                String sessionId = tokenService.requestAndRegisterToken(oauth_token, verifier);
                String baseUrl = String.format("%s://%s:%d", request.getScheme(), request.getServerName(), request.getServerPort());
                Cookie sessionId1 = new Cookie("sessionId", sessionId);
                sessionId1.setMaxAge(60*60);
                sessionId1.setPath("/");
                response.addCookie(sessionId1);
                response.sendRedirect(baseUrl);
                return sessionId;

            } else {
                String baseUrl = String.format("%s://%s:%d/signin", request.getScheme(), request.getServerName(), request.getServerPort());
                response.sendRedirect(tokenService.getAuthentificationUrl(baseUrl));
            }
        } catch (IOException e) {
            logger.error("Problem with callBack user ", e);
        }
        return null;
    }
}
