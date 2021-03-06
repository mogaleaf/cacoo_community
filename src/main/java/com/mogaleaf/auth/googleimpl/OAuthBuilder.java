package com.mogaleaf.auth.googleimpl;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.HttpTransport;
import com.mogaleaf.auth.Transport;
import com.mogaleaf.helper.PropertieHelper;

/**
 * Build the signer for Oauth.
 */
public class OAuthBuilder {

    protected OAuthHmacSigner oAuthHmacSigner = new OAuthHmacSigner();
    protected HttpTransport netHttpTransport;
    protected static String consumerKey = System.getenv("ConsumerKey");
    static{
       if(consumerKey == null){
           consumerKey = PropertieHelper.appProperties.getProperty("ConsumerKey");
       }
    }

    public OAuthBuilder(Transport transport) {
        netHttpTransport = transport.getHttpTransport();
        oAuthHmacSigner.clientSharedSecret = System.getenv("ConsumerSecret");
        if(oAuthHmacSigner.clientSharedSecret == null){
            oAuthHmacSigner.clientSharedSecret = PropertieHelper.appProperties.getProperty("ConsumerSecret");
        }
    }
}
