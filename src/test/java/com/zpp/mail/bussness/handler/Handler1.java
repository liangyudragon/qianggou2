package com.zpp.mail.bussness.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.bussness.Main;
import com.zpp.mail.bussness.constants.InitConstants;
import com.zpp.mail.utils.HttpClientUtil;

public class Handler1 extends Thread{
    private static final Logger log = LoggerFactory.getLogger(Handler1.class);

    private String eventId = null;
    private String eventType = null; 
    private AtomicInteger count = null; 

    public Handler1 (String eventId, String eventType, AtomicInteger count) {
        super();
        this.eventId = eventId;
        this.eventType = eventType;
        this.count = count;
    }

    public void run() {
        if (count.get() > Main.loopCount) {
            log.debug("count.get() is ::::"+String.valueOf(count.get()));
            return;
        }
        // 参考 Test2.java
        //新的创建httpClient的方法
        CloseableHttpClient httpclient = null;
        try {
//          httpclient = HttpClients.createDefault();
            httpclient = HttpClientUtil.getNewHttpClient();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            String url = InitConstants.HANDLER1_URL;
            HttpPost httpPost = new HttpPost(url);
            httpPost.setHeader("User-Agent", InitConstants.REQUEST_AGENT);
            httpPost.setHeader("Referer", InitConstants.HANDLER1_REFERER);
            httpPost.setHeader("Accept", InitConstants.REQUEST_ACCEPT);
            httpPost.setHeader("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpPost.setHeader("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpPost.setHeader("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpPost.setHeader("Connection", InitConstants.REQUEST_CONNECTION);
            httpPost.setHeader("Host", InitConstants.REQUEST_HOST);
            httpPost.setHeader("Pragma", InitConstants.REQUEST_PRAGMA);

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();

            nvps.add(new BasicNameValuePair("mail1", InitConstants.HANDLER1_EMAIL));
            nvps.add(new BasicNameValuePair("mail2", InitConstants.HANDLER1_EMAIL));
            nvps.add(new BasicNameValuePair("event_id", this.eventId));
            nvps.add(new BasicNameValuePair("event_type", this.eventType));
            CloseableHttpResponse response2 = null;
            try {
                httpPost.setEntity(new UrlEncodedFormEntity(nvps));
                boolean responseError = true;
                while (responseError) {
                    
                    response2 = httpclient.execute(httpPost);
                    if (!response2.getStatusLine().toString().contains("200")) {
                        log.info("响应出错，请求eventId= ["+ eventId + "],  的响应状态：" + response2.getStatusLine().toString());
                        try {
                            Thread.currentThread().sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        responseError = false;
                    }
                }


                // 打印http响应
                HttpEntity httpEntity2 =response2.getEntity();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(httpEntity2.getContent(), "Shift_JIS"));
                String line2;
                while((line2 = br2.readLine()) != null){
                    log.debug(line2);
                    if (line2.contains("ご注文予約｜メール送信｜完了")) {
                        log.info("请求eventId= ["+ eventId + "],  ご注文予約｜メール送信｜完了");
                        count.getAndIncrement();
                        break;
                    }
                    if (line2.contains("ただいま満席のため、応募受付ができません。")) {
                        log.info("请求eventId= ["+ eventId + "],  ただいま満席のため、応募受付ができません。");
                        count.set(0);
                        break;
                    }
                }
                
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } catch (UnsupportedEncodingException e) {
                log.error(e.getMessage(), e);
            } catch (ClientProtocolException e) {
                log.error(e.getMessage(), e);
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            } finally {
                if (response2 != null) {
                    try {
                        response2.close();
                    } catch (IOException e) {
                        log.debug(e.getMessage(), e);
                    }
                }
            }
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.debug(e.getMessage(), e);
            }
        }
    }
}
