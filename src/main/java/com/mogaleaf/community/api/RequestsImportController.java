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
public class RequestsImportController {

    @Autowired
    private TokenService tokenService;

    @Autowired
    private DiagramsService diagramsService;

    static Logger logger = LoggerFactory.getLogger(RequestsImportController.class);


    @RequestMapping("/user/import")
    public String importDiagrams(HttpServletRequest request, HttpServletResponse response, @RequestParam("sessionId") String sessionId) {
        logger.debug("[User import]");
        try {
            if (!tokenService.loggedUser(sessionId)) {
                String baseUrl = String.format("%s://%s:%d/signup/new", request.getScheme(), request.getServerName(), request.getServerPort());
                response.sendRedirect(baseUrl);
            } else {
                diagramsService.retrieveTemplateDiagrams(sessionId);
            }
        } catch (IOException e) {
            logger.error("Problem with importDiagrams user ", e);
        }
        return "Imported.";
    }

}