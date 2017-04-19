package com.mogaleaf.community.api;

import com.mogaleaf.community.api.service.DiagramsService;
import com.mogaleaf.community.model.Diagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


@RestController
public class RequestsAppliController {

    @Autowired
    DiagramsService diagramsService;

    static Logger logger = LoggerFactory.getLogger(RequestsAppliController.class);

    @RequestMapping("/api/rate")
    public void rate(@RequestParam("diagId") String diagId, @RequestParam(value = "score", defaultValue = "0") String score) {
        if(diagId == null || diagId.isEmpty()){
            throw new IllegalArgumentException("DiagId not present");
        }
        logger.debug("Rate DiagId: {} with score: {}", diagId, score);
        int scoreInt = Integer.valueOf(score);
        if (scoreInt > 5) {
            scoreInt = 5;
        } else if (scoreInt < 0) {
            scoreInt = 0;
        }
        diagramsService.rate(diagId, scoreInt);
    }


    @RequestMapping("/api/popular")
    @ResponseBody
    public List<Diagram> popular(@RequestParam(value = "max" , defaultValue = "10") String max) {
        Integer maxInt;
        try {
            maxInt = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("max is not an Integer");
        }
        try {
            return diagramsService.retrieveMostPopular(maxInt);
        } catch (Exception e) {
            logger.error("Problem during popular retrieving", e);
            throw new RuntimeException("Problem during popular retrieving");
        }
    }


    @RequestMapping("/api/recent")
    @ResponseBody
    public List<Diagram> recent(@RequestParam(value = "max", defaultValue = "10") String max) {
        Integer maxInt;
        try {
            maxInt = Integer.parseInt(max);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("max is not an Integer");
        }
        try {
            return diagramsService.retrieveMostRecent(maxInt);
        } catch (Exception e) {
            logger.error("Problem during recent retrieving", e);
            throw new RuntimeException("Problem during recent retrieving");
        }
    }



    @ExceptionHandler
    void handleIllegalArgumentException(IllegalArgumentException e, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(),e.getMessage());
    }

}
