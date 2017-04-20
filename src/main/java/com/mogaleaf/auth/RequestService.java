package com.mogaleaf.auth;

/**
 * Get the service to execute the request.
 */
public interface RequestService {

    public ExecuteRequest getService(UserToken token);
}
