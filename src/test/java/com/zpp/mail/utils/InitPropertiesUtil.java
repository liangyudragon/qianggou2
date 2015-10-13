package com.zpp.mail.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class InitPropertiesUtil {
    private static final Logger log = LoggerFactory.getLogger(InitPropertiesUtil.class);

    private static InitPropertiesUtil propertiesFactory = null;
    // 配置文件
    private static Properties staticInitProps = null;
    
    private InitPropertiesUtil(String filePath){
        if (staticInitProps == null) {
            staticInitProps = new Properties();
            try {
                staticInitProps.load(new InputStreamReader(InitPropertiesUtil.class.getResourceAsStream(filePath),"UTF-8"));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static InitPropertiesUtil getInstance(String filePath) {
        if (propertiesFactory == null) {
            propertiesFactory = new InitPropertiesUtil(filePath);
        }
        return propertiesFactory;
    }

    public static String getInitProperty(String key) {
        return propertiesFactory.staticInitProps.getProperty(key);
    }
}
