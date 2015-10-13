package com.zpp.mail.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.utils.InitPropertiesUtil;

/**
 * 测试log4j异常输出
 * @author weiqz
 *
 */
public class Test91 {

    private static final Logger log = LoggerFactory.getLogger(Test91.class);

    public static void main(String[] args) {
        try {
            Properties staticInitProps = null;
//            String initFilePath = "/init.properties";
            String initFilePath = "/hahahaha.properties";
            staticInitProps.load(new InputStreamReader(InitPropertiesUtil.class.getResourceAsStream(initFilePath),"UTF-8"));
        } catch (FileNotFoundException e) {
            log.debug("出错了FileNotFoundException");
            log.debug(e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            log.debug("出错了IOException");
            log.debug(e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
//            log.debug(e.getCause().toString());
            log.debug(e.getMessage(), e);
        }
    }

}
