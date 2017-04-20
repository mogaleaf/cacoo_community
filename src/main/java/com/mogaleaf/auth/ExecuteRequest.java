package com.mogaleaf.auth;

import java.io.IOException;
import java.io.InputStream;

/**
 * Execute a request for an Oauth api.
 */
public interface ExecuteRequest {
    InputStream executeRequest(String url) throws IOException;
}
