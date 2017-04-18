package com.mogaleaf.auth;

import java.io.IOException;
import java.io.InputStream;

public interface ExecuteRequest {
    InputStream executeRequest(String url) throws IOException;
}
