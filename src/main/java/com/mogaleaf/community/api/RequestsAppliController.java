package com.mogaleaf.community.api;

import com.mogaleaf.community.api.service.DiagramsService;
import com.mogaleaf.community.model.Diagram;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
public class RequestsAppliController {

    @Autowired
    DiagramsService diagramsService;

    Logger logger = LoggerFactory.getLogger(RequestsAppliController.class);

    @RequestMapping("/api/rate")
    public void rate(@RequestParam("diagId") String diagId, @RequestParam(value = "score", defaultValue = "0") String score) {
        logger.debug("Rate DiagId: {} with score: {}",diagId,score);
        int scoreInt =  Integer.valueOf(score);
        if(scoreInt > 5){
            scoreInt = 5;
        } else if(scoreInt < 0){
            scoreInt = 0;
        }
        diagramsService.rate(diagId, scoreInt);
    }


    @RequestMapping("/api/popular")
    public
    @ResponseBody
    List<Diagram> popular() {
        return diagramsService.retrieveMostPopular(10);
    }


    @RequestMapping("/api/recent")
    public
    @ResponseBody
    List<Diagram> recent() {
        return diagramsService.retrieveMostRecent(10);
    }


}
