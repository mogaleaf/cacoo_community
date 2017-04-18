package com.mogaleaf.community.db;

import com.mogaleaf.auth.UserToken;
import com.mogaleaf.community.model.Diagram;

import java.util.List;


public interface DatabaseService {

    void registerCredential(String email,UserToken userToken) ;
    UserToken retrieveCredential(String email) ;
    boolean logUser(String email);
    boolean curLogUser(String email);

    void addDiagrams(List<Diagram> retrieveDiags);

    List<Diagram> retrieveMostPopular(int i);

    List<Diagram> retrieveMostRecent(int i);

    Diagram retrieve(String diagId);

    void update(Diagram diag);
}
