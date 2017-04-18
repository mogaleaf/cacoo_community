package com.mogaleaf.auth;

import com.mogaleaf.auth.googleimpl.GoogleApiExecuteRequest;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;


public interface RequestService {

    public ExecuteRequest getService(UserToken token);
}
