package com.zpp.mail.bussness.constants;

import com.zpp.mail.utils.InitPropertiesUtil;

public class InitConstants {

    public static final String REQUEST_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36";
    public static final String REQUEST_ACCEPT = "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8";
    public static final String REQUEST_ACCEPT_ENCODING = "gzip, deflate, sdch";
    public static final String REQUEST_Accept_Language = "zh-CN,zh;q=0.8";
    public static final String REQUEST_Cache_Control = "no-cache";
    public static final String REQUEST_CONNECTION = "keep-alive";
    public static final String REQUEST_HOST = "aksale.advs.jp";
    public static final String REQUEST_PRAGMA = "no-cache";
    public static final String REQUEST_UPGRADE_INSECURE_REQUESTS = "1";
    public static final String REQUEST_ = "";

    public static final String INIT_FILE_PATH = "/init.properties";
    public static final String INIT_FILE_PATH_101 = "/init101.properties";
    public static final String INIT_FILE_PATH_102 = "/init102.properties";
    public static final String INIT_FILE_PATH_103 = "/init103.properties";
    public static final String INIT_FILE_PATH_104 = "/init104.properties";

    public static final String HANDLER1_URL = InitPropertiesUtil.getInitProperty("handle1.url");
    public static final String HANDLER1_REFERER = InitPropertiesUtil.getInitProperty("handle1.url.referer");
    public static final String HANDLER1_EMAIL = InitPropertiesUtil.getInitProperty("handle1.mail");

    public static final String HANDLER2_USERNAME = InitPropertiesUtil.getInitProperty("handle2.username");
    public static final String HANDLER2_PASSWORD = InitPropertiesUtil.getInitProperty("handle2.password");

    public static final String HANDLE3_URL1 = InitPropertiesUtil.getInitProperty("handle3.url1");
    public static final String HANDLE3_URL2 = InitPropertiesUtil.getInitProperty("handle3.url2");
    public static final String HANDLE3_URL3 = InitPropertiesUtil.getInitProperty("handle3.url3");
    public static final String HANDLE3_URL4 = InitPropertiesUtil.getInitProperty("handle3.url4");
    public static final String HANDLE3_URL5 = InitPropertiesUtil.getInitProperty("handle3.url5");
    public static final String HANDLE3_REDIRECT = InitPropertiesUtil.getInitProperty("handle3.redirect");

    public static final String HANDLE3_CARD_NO = InitPropertiesUtil.getInitProperty("handle3.card_no");
    public static final String HANDLE3_PASSWORD = InitPropertiesUtil.getInitProperty("handle3.password");
    public static final String HANDLE3_SEI = InitPropertiesUtil.getInitProperty("handle3.sei");
    public static final String HANDLE3_MEI = InitPropertiesUtil.getInitProperty("handle3.mei");
    public static final String HANDLE3_SEI_KANA = InitPropertiesUtil.getInitProperty("handle3.sei_kana");
    public static final String HANDLE3_MEI_KANA = InitPropertiesUtil.getInitProperty("handle3.mei_kana");
    public static final String HANDLE3_TEL1 = InitPropertiesUtil.getInitProperty("handle3.tel1");
    public static final String HANDLE3_TEL2 = InitPropertiesUtil.getInitProperty("handle3.tel2");
    public static final String HANDLE3_TEL3 = InitPropertiesUtil.getInitProperty("handle3.tel3");
}
