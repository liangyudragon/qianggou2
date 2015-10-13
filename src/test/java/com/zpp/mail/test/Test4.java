package com.zpp.mail.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试类--请求完成预约
 * 
 */
public class Test4 {
    private static final Logger log = LoggerFactory.getLogger(Test4.class);

    public final static void main(String[] args) throws Exception {
        //新的创建httpClient的方法
        CloseableHttpClient httpclient = HttpClients.createDefault();
        
        String random = "Djrjfj0NtcgnuY5ATZqJmjx6q2GsUTkD";
        try {
            // 创建一个本地Cookie存储的实例
            CookieStore cookieStore = new BasicCookieStore();
            //创建一个本地上下文信息
            HttpContext localContext = new BasicHttpContext();
            //在本地上下问中绑定一个本地存储
            localContext.setAttribute(HttpClientContext.COOKIE_STORE, cookieStore);


            // -----------------------激活认证邮件
            HttpGet httpGet = new HttpGet("https://aksale.advs.jp/cp/akachan_sale_pc/reg?id=" + random);
            CloseableHttpResponse response1 = httpclient.execute(httpGet, localContext);

            //获取cookie中的各种信息
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                log.debug("Local cookie: " + cookies.get(i));
            }

            // 打印http响应
            HttpEntity httpEntity1 =response1.getEntity();
            BufferedReader br1 = new BufferedReader(new InputStreamReader(httpEntity1.getContent(), "Shift_JIS"));
            String line1;
            while((line1 = br1.readLine()) != null){
                log.debug(line1);
                // TODO 判断如果有error中断线程
            }
 
            try {
                System.out.println(response1.getStatusLine());
                HttpEntity entity1 = response1.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity1);
            } finally {
                response1.close();
            }
            
            
            

            // -----------------------填写 [カード番号/仮ID認証]
            HttpPost httpPost = new HttpPost("https://aksale.advs.jp/cp/akachan_sale_pc/form_card_no.cgi");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            httpPost.setHeader("Referer", "https://aksale.advs.jp/cp/akachan_sale_pc/_reg_form.cgi?id=" + random);

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();

            nvps.add(new BasicNameValuePair("card_no", "2800054779666"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost, localContext);

            // 打印http响应
            HttpEntity httpEntity2 =response2.getEntity();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(httpEntity2.getContent(), "Shift_JIS"));
            String line2;
            while((line2 = br2.readLine()) != null){
                log.debug(line2);
                // TODO 判断如果有error中断线程
            }
 
            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
            
            

            // -----------------------填写 [个人信息]
            HttpPost httpPost3 = new HttpPost("https://aksale.advs.jp/cp/akachan_sale_pc/reg_form_event_1.cgi");
            httpPost3.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            httpPost3.setHeader("Referer", "https://aksale.advs.jp/cp/akachan_sale_pc/form_card_no.cgi");

            nvps = new ArrayList <NameValuePair>();

            nvps.add(new BasicNameValuePair("password", "1314"));
            nvps.add(new BasicNameValuePair("sei", "TANG"));
            nvps.add(new BasicNameValuePair("mei", "ZIQI"));
            nvps.add(new BasicNameValuePair("sei_kana", "TANG"));
            nvps.add(new BasicNameValuePair("mei_kana", "ZIQI"));
            nvps.add(new BasicNameValuePair("tel1", "022"));
            nvps.add(new BasicNameValuePair("tel2", "3795"));
            nvps.add(new BasicNameValuePair("tel3", "962"));

            httpPost3.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response3 = httpclient.execute(httpPost3, localContext);

            // 打印http响应
            HttpEntity httpEntity3 =response3.getEntity();
            BufferedReader br3 = new BufferedReader(new InputStreamReader(httpEntity3.getContent(), "Shift_JIS"));
            String line3;
            while((line3 = br3.readLine()) != null){
                log.debug(line3);
                // TODO 判断如果有error中断线程
            }
 
            try {
                System.out.println(response3.getStatusLine());
                HttpEntity entity3 = response3.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity3);
            } finally {
                response3.close();
            }
            

            // -----------------------[发送确认邮件]
            HttpPost httpPost4 = new HttpPost("https://aksale.advs.jp/cp/akachan_sale_pc/reg_confirm_event.cgi");
            httpPost4.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            httpPost4.setHeader("Referer", "https://aksale.advs.jp/cp/akachan_sale_pc/reg_form_event_1.cgi");
            CloseableHttpResponse response4 = httpclient.execute(httpPost4, localContext);

            // 打印http响应
            HttpEntity httpEntity4 =response4.getEntity();
            BufferedReader br4 = new BufferedReader(new InputStreamReader(httpEntity4.getContent(), "Shift_JIS"));
            String line4;
            while((line4 = br4.readLine()) != null){
                log.debug(line4);
                // TODO 判断如果有error中断线程
            }
 
            try {
                System.out.println(response4.getStatusLine());
                HttpEntity entity4 = response4.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity4);
            } finally {
                response4.close();
            }
        } finally {
            httpclient.close();
        }

    }

}
