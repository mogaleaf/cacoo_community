package com.mogaleaf.community.api.service;

import com.mogaleaf.auth.RequestService;
import com.mogaleaf.auth.UserToken;
import com.mogaleaf.cacoo.api.DiagramsBuilder;
import com.mogaleaf.cacoo.model.Diagrams;
import com.mogaleaf.cacoo.model.Result;
import com.mogaleaf.community.db.DatabaseService;
import com.mogaleaf.community.model.Diagram;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class DiagramsService {

    @Autowired
    private DatabaseService database;

    @Autowired
    private RequestService requestService;

    @Async
    public void retrieveTemplateDiagrams(String email) throws IOException {
        UserToken token = database.retrieveCredential(email);
        DiagramsBuilder builder = new DiagramsBuilder(requestService.getService(token));
        builder.setType(DiagramsBuilder.Type.template).setLimit(10).setSortOn(DiagramsBuilder.SortOn.updated);
        Diagrams build = builder.build();
        List<Diagram> retrieveDiags = new ArrayList<>();
        if (build != null && build.result != null) {
            for (Result result : build.result) {
                retrieveDiags.add(buildDiag(result));
            }
        }
        database.addDiagrams(retrieveDiags);
    }

    private Diagram buildDiag(Result result) {
        Diagram diagram = new Diagram();
        diagram.id = result.diagramId;
        diagram.imageUrl = result.imageUrl;
        diagram.name = result.title;
        diagram.rate = 0.0;
        diagram.numberOfRate = 0;
        return diagram;
    }

    public List<Diagram> retrieveMostPopular(int i) {
        return database.retrieveMostPopular(i);
    }

    public List<Diagram> retrieveMostRecent(int i) {
        return database.retrieveMostRecent(i);
    }

    public void rate(String diagId, int scoreInt) {
        //TODO Add a redis script to calc the new rate and update it so we dont have sync issue
        Diagram diag = database.retrieve(diagId);
        diag.rate = (diag.rate * diag.numberOfRate + scoreInt) / (diag.numberOfRate + 1);
        diag.numberOfRate = diag.numberOfRate + 1;
        database.update(diag);
    }
}
