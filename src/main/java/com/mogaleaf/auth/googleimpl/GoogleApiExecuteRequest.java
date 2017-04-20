package com.mogaleaf.auth.googleimpl;

import com.google.api.client.auth.oauth.OAuthParameters;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpResponse;
import com.mogaleaf.auth.ExecuteRequest;
import com.mogaleaf.auth.Transport;
import com.mogaleaf.auth.UserToken;

import java.io.IOException;
import java.io.InputStream;

public class GoogleApiExecuteRequest implements ExecuteRequest {

    OAuthBuilder oAuthBuilder;
    OAuthParameters parameters = new OAuthParameters();

    public GoogleApiExecuteRequest(Transport transport, UserToken userToken) {
        oAuthBuilder = new OAuthBuilder(transport);
        parameters.consumerKey = oAuthBuilder.consumerKey;
        parameters.signer = oAuthBuilder.oAuthHmacSigner;
        parameters.token = userToken.token;
        oAuthBuilder.oAuthHmacSigner.tokenSharedSecret = userToken.tokenSecret;
    }

    /**
     * Sign the request to contact cacoo api.
     *
     * @param url
     * @return
     * @throws IOException
     */
    @Override
    public InputStream executeRequest(String url) throws IOException {
        HttpRequestFactory requestFactory = oAuthBuilder.netHttpTransport.createRequestFactory(parameters);
        GenericUrl genericUrl = new GenericUrl(url);
        HttpRequest httpRequest = requestFactory.buildGetRequest(genericUrl);
        HttpResponse response = httpRequest.execute();
        return response.getContent();
    }
}
