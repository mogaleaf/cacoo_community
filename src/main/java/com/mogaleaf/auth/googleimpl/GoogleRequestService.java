package com.mogaleaf.auth.googleimpl;

import com.mogaleaf.auth.ExecuteRequest;
import com.mogaleaf.auth.RequestService;
import com.mogaleaf.auth.Transport;
import com.mogaleaf.auth.UserToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Define the transport to use.
 */
@Service
public class GoogleRequestService implements RequestService {

    @Autowired
    Transport transport;

    @Override
    public ExecuteRequest getService(UserToken token) {
        return new GoogleApiExecuteRequest(transport, token);
    }
}
