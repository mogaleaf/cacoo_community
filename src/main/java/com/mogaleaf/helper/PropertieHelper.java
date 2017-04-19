package com.mogaleaf.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

public class PropertieHelper {

    static Logger logger = LoggerFactory.getLogger(PropertieHelper.class);
    public static Properties appProperties = new Properties();
    public static Properties urlProperties = new Properties();
    static{

        try {
            appProperties.load(PropertieHelper.class.getClassLoader().getResourceAsStream("app.properties"));
            urlProperties.load(PropertieHelper.class.getClassLoader().getResourceAsStream("url.properties"));
        }catch(Exception e){
            logger.error("Load appProperties problem", e);
        }
    }
}
