package com.mogaleaf.community.api;

import com.mogaleaf.auth.TokenService;
import com.mogaleaf.community.api.service.DiagramsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
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

    /**
     * Import template diagrams from a user who must have authorized the application on cacoo.
     */
    @RequestMapping("/user/import")
    public String importDiagrams(HttpServletRequest request, HttpServletResponse response, @CookieValue(value = "sessionId", defaultValue = "none") String sessionId) {
        logger.debug("[User import]");
        if (sessionId == null || sessionId.isEmpty()) {
            throw new IllegalArgumentException("sessionId not present, please log fisrt");
        }
        try {
            if (sessionId.equals("none") || !tokenService.loggedUser(sessionId)) {
                String baseUrl = String.format("%s://%s:%d/signup/new", request.getScheme(), request.getServerName(), request.getServerPort());
                response.sendRedirect(baseUrl);
            } else {
                diagramsService.retrieveTemplateDiagrams(sessionId);
            }
        } catch (Exception e) {
            logger.error("Problem with importDiagrams user ", e);
        }
        return "Imported.";
    }

    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), e.getMessage());
    }
}
