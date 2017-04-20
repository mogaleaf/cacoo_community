package com.mogaleaf.auth;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.stereotype.Component;

/**
 * Transport use to execute Oauth request.
 */
@Component
public class Transport {

    private NetHttpTransport httpTransport = new NetHttpTransport();

    public HttpTransport getHttpTransport(){
        return httpTransport;
    }
}
