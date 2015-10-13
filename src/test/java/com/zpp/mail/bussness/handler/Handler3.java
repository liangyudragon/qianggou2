package com.zpp.mail.bussness.handler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.bussness.constants.InitConstants;
import com.zpp.mail.utils.HttpClientUtil;

public class Handler3 implements Runnable{
    private static final Logger log = LoggerFactory.getLogger(Handler3.class);
    
    private ConcurrentLinkedQueue<String> queue = null;
    private boolean okFlag = false;
    private String random = "Djrjfj0NtcgnuY5ATZqJmjx6q2GsUTkD";
    public Handler3(ConcurrentLinkedQueue<String> queue) {
        this.queue = queue;
    }  
    
    @Override
    public void run() {
        
        //新的创建httpClient的方法
//        CloseableHttpClient httpclient = HttpClients.createDefault();
        CloseableHttpClient httpclient = HttpClientUtil.getNewHttpClient();
        
        
        if (queue.isEmpty()) {
            return;
        }
        random = queue.poll();
        log.info("wwwwwwwwwwwwwwwwwwww： 邮件随机数： " + random);

        try {
            // 创建一个本地Cookie存储的实例
            CookieStore cookieStore = new BasicCookieStore();
            //创建一个本地上下文信息
            HttpContext localContext = new BasicHttpContext();
            //在本地上下问中绑定一个本地存储
            localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);


            // ----------------------------------------------激活认证邮件
            HttpGet httpGet = new HttpGet(InitConstants.HANDLE3_URL1 + random);
            httpGet.setHeader("User-Agent", InitConstants.REQUEST_AGENT);
            httpGet.setHeader("Accept", InitConstants.REQUEST_ACCEPT);
            httpGet.setHeader("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpGet.setHeader("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpGet.setHeader("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpGet.setHeader("Connection", InitConstants.REQUEST_CONNECTION);
            httpGet.setHeader("Host", InitConstants.REQUEST_HOST);
            httpGet.setHeader("Upgrade-Insecure-Requests", InitConstants.REQUEST_UPGRADE_INSECURE_REQUESTS);
            httpGet.setHeader("Pragma", InitConstants.REQUEST_PRAGMA);
            
            boolean loopFlag = true; // 循环请求标示位，为true时表示当前线程执行的url请求需要再次执行
            boolean breakFlag = false; // 跳出循环标识位，为true时表示可以跳出循环
            int breakTime = 0;
            int loopCount = 0;
            while (loopFlag) {
                if (loopCount != 0) {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
                CloseableHttpResponse response1 = httpclient.execute(httpGet, localContext);
    
                //获取cookie中的各种信息
                List<Cookie> cookies = cookieStore.getCookies();
                for (int i = 0; i < cookies.size(); i++) {
                    log.debug("邮件随机数： "+ random + " , Local cookie: " + cookies.get(i));
                }
    
                // 打印http响应
                HttpEntity httpEntity1 =response1.getEntity();
                BufferedReader br1 = new BufferedReader(new InputStreamReader(httpEntity1.getContent(), "Shift_JIS"));
                String line1;
                loopFlag = false;
                while((line1 = br1.readLine()) != null){
                    log.debug("邮件随机数： "+ random + " , " + line1);
                    // 判断如果有error中断线程
                    if (line1.contains("error")) {
                        breakFlag = true;
                        loopCount ++;
                        loopFlag = true;
                    }
                    if (breakFlag) {
                        breakTime++;

                        if (line1.contains("既にご予約済みですので、新規予約ができません。")) {
                            okFlag = true;
                            return;
                        }
                    }
                    if (breakTime > 6) {
                        loopFlag = true;
                        break;
                    }
                }

                try {
                    if (!response1.getStatusLine().toString().contains("200")) {
                        log.info(response1.getStatusLine().toString());
                    }
                    log.info(response1.getStatusLine().toString());
                    HttpEntity entity1 = response1.getEntity();
                    // do something useful with the response body
                    // and ensure it is fully consumed
                    EntityUtils.consume(entity1);
                } finally {
                    response1.close();
                }

            }
            
            

            // ----------------------------------------------填写 [カード番号/仮ID認証]
            HttpPost httpPost2 = new HttpPost(InitConstants.HANDLE3_URL2);
            httpPost2.setHeader("User-Agent", InitConstants.REQUEST_AGENT);
            httpPost2.setHeader("Referer", InitConstants.HANDLE3_URL3 + random);
            httpPost2.setHeader("Accept", InitConstants.REQUEST_ACCEPT);
            httpPost2.setHeader("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpPost2.setHeader("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpPost2.setHeader("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpPost2.setHeader("Connection", InitConstants.REQUEST_CONNECTION);
            httpPost2.setHeader("Host", InitConstants.REQUEST_HOST);
            httpPost2.setHeader("Upgrade-Insecure-Requests", InitConstants.REQUEST_UPGRADE_INSECURE_REQUESTS);
            httpPost2.setHeader("Pragma", InitConstants.REQUEST_PRAGMA);

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();

            nvps.add(new BasicNameValuePair("card_no", InitConstants.HANDLE3_CARD_NO));
            httpPost2.setEntity(new UrlEncodedFormEntity(nvps));

            breakFlag = false;
            loopFlag = true;
            breakTime = 0;
            loopCount = 0;
            while (loopFlag) {
                if (loopCount != 0) {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
//                if (loopCount > 10) {
//                    // 同一请求重复十次时，退出；
//                    log.info("♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢请求： "+ random + " 的response2超过规定次数，退出运行。");
//                    return;
//                }
                CloseableHttpResponse response2 = httpclient.execute(httpPost2, localContext);

                // 打印http响应
                HttpEntity httpEntity2 =response2.getEntity();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(httpEntity2.getContent(), "Shift_JIS"));
                String line2;
                loopFlag = false;
                while((line2 = br2.readLine()) != null){
                    log.debug("邮件随机数： "+ random + " , " + line2);
                    // 判断如果有error中断线程
                    if (line2.contains("error")) {
                        breakFlag = true;
                        loopCount ++;
                        loopFlag = true;
                    }
                    if (breakFlag) {
                        breakTime++;

                        if (line2.contains("既にご予約済みですので、新規予約ができません。")) {
                            okFlag = true;
                            return;
                        }
                    }
                    if (breakTime > 6) {
                        loopFlag = true;
                        break;
                    }
                }

                try {
                    if (!response2.getStatusLine().toString().contains("200")) {
                        log.info(response2.getStatusLine().toString());
                    }
                    log.info(response2.getStatusLine().toString());
                    HttpEntity entity2 = response2.getEntity();
                    // do something useful with the response body
                    // and ensure it is fully consumed
                    EntityUtils.consume(entity2);
                } finally {
                    response2.close();
                }
            }

            
            

            // ----------------------------------------------填写 [个人信息]
            HttpPost httpPost3 = new HttpPost(InitConstants.HANDLE3_URL4);
            httpPost3.setHeader("User-Agent", InitConstants.REQUEST_AGENT);
            httpPost3.setHeader("Referer", InitConstants.HANDLE3_URL2);
            httpPost3.setHeader("Accept", InitConstants.REQUEST_ACCEPT);
            httpPost3.setHeader("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpPost3.setHeader("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpPost3.setHeader("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpPost3.setHeader("Connection", InitConstants.REQUEST_CONNECTION);
            httpPost3.setHeader("Host", InitConstants.REQUEST_HOST);
            httpPost3.setHeader("Upgrade-Insecure-Requests", InitConstants.REQUEST_UPGRADE_INSECURE_REQUESTS);
            httpPost3.setHeader("Pragma", InitConstants.REQUEST_PRAGMA);

            nvps = new ArrayList <NameValuePair>();

            nvps.add(new BasicNameValuePair("password", InitConstants.HANDLE3_PASSWORD));
            nvps.add(new BasicNameValuePair("sei", InitConstants.HANDLE3_SEI));
            nvps.add(new BasicNameValuePair("mei", InitConstants.HANDLE3_MEI));
            nvps.add(new BasicNameValuePair("sei_kana", InitConstants.HANDLE3_SEI_KANA));
            nvps.add(new BasicNameValuePair("mei_kana", InitConstants.HANDLE3_MEI_KANA));
            nvps.add(new BasicNameValuePair("tel1", InitConstants.HANDLE3_TEL1));
            nvps.add(new BasicNameValuePair("tel2", InitConstants.HANDLE3_TEL2));
            nvps.add(new BasicNameValuePair("tel3", InitConstants.HANDLE3_TEL3));

            httpPost3.setEntity(new UrlEncodedFormEntity(nvps));
            
            // 循环请求标示位
            breakFlag = false;
            loopFlag = true;
            breakTime = 0;
            loopCount = 0;
            while (loopFlag) {
                if (loopCount != 0) {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
//                if (loopCount > 10) {
//                    // 同一请求重复十次时，退出；
//                    log.info("♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢请求： "+ random + " 的response3超过规定次数，退出运行。");
//                    return;
//                }
                CloseableHttpResponse response3 = httpclient.execute(httpPost3, localContext);

                // 打印http响应
                HttpEntity httpEntity3 =response3.getEntity();
                BufferedReader br3 = new BufferedReader(new InputStreamReader(httpEntity3.getContent(), "Shift_JIS"));
                String line3;
                loopFlag = false;
                while((line3 = br3.readLine()) != null){
                    log.debug("邮件随机数： "+ random + " , " + line3);
                    // 判断如果有error中断线程
                    if (line3.contains("error")) {
                        breakFlag = true;
                        loopCount ++;
                        loopFlag = true;
                    }
                    if (breakFlag) {
                        breakTime++;

                        if (line3.contains("既にご予約済みですので、新規予約ができません。")) {
                            okFlag = true;
                            return;
                        }
                    }
                    if (breakTime > 6) {
                        loopFlag = true;
                        break;
                    }
                }
     
                try {
                    if (!response3.getStatusLine().toString().contains("200")) {
                        log.info(response3.getStatusLine().toString());
                    }
                    log.info(response3.getStatusLine().toString());
                    HttpEntity entity3 = response3.getEntity();
                    // do something useful with the response body
                    // and ensure it is fully consumed
                    EntityUtils.consume(entity3);
                } finally {
                    response3.close();
                }
                
                
            }

            

            // ----------------------------------------------[发送确认邮件]
            HttpPost httpPost4 = new HttpPost(InitConstants.HANDLE3_URL5);
            httpPost4.setHeader("User-Agent", InitConstants.REQUEST_AGENT);
            httpPost4.setHeader("Referer", InitConstants.HANDLE3_URL4);
            httpPost4.setHeader("Accept", InitConstants.REQUEST_ACCEPT);
            httpPost4.setHeader("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpPost4.setHeader("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpPost4.setHeader("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpPost4.setHeader("Connection", InitConstants.REQUEST_CONNECTION);
            httpPost4.setHeader("Host", InitConstants.REQUEST_HOST);
            httpPost4.setHeader("Upgrade-Insecure-Requests", InitConstants.REQUEST_UPGRADE_INSECURE_REQUESTS);
            httpPost4.setHeader("Pragma", InitConstants.REQUEST_PRAGMA);
            // 循环请求标示位
            breakFlag = false;
            loopFlag = true;
            breakTime = 0;
            loopCount = 0;
            while (loopFlag) {
                if (loopCount != 0) {
                    try {
                        Thread.currentThread().sleep(5000);
                    } catch (InterruptedException e) {
                        log.error(e.getMessage(), e);
                    }
                }
//                if (loopCount > 10) {
//                    // 同一请求重复十次时，退出；
//                    log.info("♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢♢请求： "+ random + " 的response4超过规定次数，退出运行。");
//                    return;
//                }
                CloseableHttpResponse response4 = httpclient.execute(httpPost4, localContext);
                
                // 打印http响应
                HttpEntity httpEntity4 =response4.getEntity();
                BufferedReader br4 = new BufferedReader(new InputStreamReader(httpEntity4.getContent(), "Shift_JIS"));
                String line4;
                loopFlag = false;
                while((line4 = br4.readLine()) != null){
                    log.debug("邮件随机数： "+ random + " , " + line4);
                    if (line4.contains("ご注文予約｜応募完了")) {
                        log.debug("邮件随机数： "+ random + " , " + "-----------------------------------预约成功。");
                        okFlag = true;
                        return;
                    }
                    // 判断如果有error中断线程
                    if (line4.contains("error")) {
                        breakFlag = true;
                        loopCount ++;
                        loopFlag = true;
                    }
                    if (breakFlag) {
                        breakTime++;

                        if (line4.contains("既にご予約済みですので、新規予約ができません。")) {
                            okFlag = true;
                            return;
                        }
                    }
                    if (breakTime > 6) {
                        loopFlag = true;
                        break;
                    }
                }
                
                try {
                    if (!response4.getStatusLine().toString().contains("200")) {
                        log.info(response4.getStatusLine().toString());
                    }
                    log.info(response4.getStatusLine().toString());
                    HttpEntity entity4 = response4.getEntity();
                    // do something useful with the response body
                    // and ensure it is fully consumed
                    EntityUtils.consume(entity4);
                } finally {
                    response4.close();
                }
            }
            okFlag = true;
        } catch (UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
        } catch (ClientProtocolException e) {
            log.error(e.getMessage(), e);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
        } finally {
            try {
                httpclient.close();
            } catch (IOException e) {
                log.error(e.getMessage(), e);
            }
            if (!okFlag) {
                // 当程序异常结束时，将key重新放入队列
                queue.offer(random);
            }
        }

    
        
    }
}
