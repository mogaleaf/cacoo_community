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
public class RequestsUsersController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DiagramsService diagramsService;

    static Logger logger = LoggerFactory.getLogger(RequestsUsersController.class);


    @RequestMapping("/user/new")
    public void newUser(HttpServletRequest request,HttpServletResponse response, @RequestParam("email") String email) {
        logger.debug("[User: {}] start authentification",email);
        try {
            if (!tokenService.loggedUser(email)) {
                String baseUrl = String.format("%s://%s:%d/user/callback",request.getScheme(),  request.getServerName(), request.getServerPort());
                response.sendRedirect(tokenService.getAuthentificationUrl(email,baseUrl + "?email="+email));
            }
        } catch (IOException e) {
            logger.error("Problem with new user ",e);
        }
    }

    @RequestMapping("/user/callback")
    public String callBack(@RequestParam("email") String email, @RequestParam("oauth_verifier") String verifier) {
        logger.debug("[User: {}] accepted authorisation",email);
        try {
            if (tokenService.currentLoggingUser(email)) {
                tokenService.requestAndRegisterToken(email,verifier);
            } else if(tokenService.loggedUser(email)){
                return "User " + email + " Logged.";
            }
            else{
                return "User " + email + " fail to Logged.";
            }
        } catch (IOException e) {
            logger.error("Problem with callBack user ",e);
        }
        return "User " + email + " Logged.";
    }

    @RequestMapping("/user/import")
    public String importDiagrams(HttpServletRequest request,HttpServletResponse response,@RequestParam("email") String email) {
        logger.debug("[User: {}] import diagrams",email);
        try {
            if (!tokenService.loggedUser(email)) {
                String baseUrl = String.format("%s://%s:%d/user/new",request.getScheme(),  request.getServerName(), request.getServerPort());
                response.sendRedirect(baseUrl + "?email="+email);
            } else {
                diagramsService.retrieveTemplateDiagrams(email);
            }
        } catch (IOException e) {
            logger.error("Problem with importDiagrams user ",e);
        }
        return "User " + email + " imported.";
    }

}
