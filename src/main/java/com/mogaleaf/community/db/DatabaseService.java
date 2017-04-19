package com.mogaleaf.community.db;

import com.mogaleaf.auth.UserToken;
import com.mogaleaf.community.model.Diagram;

import java.util.List;


public interface DatabaseService {

    void registerCredential(String name,UserToken userToken) ;
    UserToken retrieveCredential(String name) ;
    boolean logUser(String name);
    boolean curLogUser(String name);

    void addDiagrams(List<Diagram> retrieveDiags);

    List<Diagram> retrieveMostPopular(int i);

    List<Diagram> retrieveMostRecent(int i);

    void rate(String diagId ,int score);

    boolean exist(String diagId);
}
