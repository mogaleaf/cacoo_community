package com.mogaleaf.auth.googleimpl;

import com.google.api.client.auth.oauth.OAuthHmacSigner;
import com.google.api.client.http.HttpTransport;
import com.mogaleaf.auth.Transport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class OAuthBuilder {
    static Logger logger = LoggerFactory.getLogger(OAuthBuilder.class);
    static Properties properties = new Properties();
    static{

        try {
            properties.load(OAuthBuilder.class.getClassLoader().getResourceAsStream("app.properties"));
        }catch(Exception e){
            logger.error("Load properties problem", e);
        }
    }
    protected OAuthHmacSigner oAuthHmacSigner = new OAuthHmacSigner();
    protected HttpTransport netHttpTransport;
    protected static String consumerKey = properties.getProperty("ConsumerKey");

    public OAuthBuilder (Transport transport){
        netHttpTransport = transport.getHttpTransport();
        oAuthHmacSigner.clientSharedSecret = properties.getProperty("ConsumerSecret");
    }
}
