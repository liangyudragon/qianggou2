package com.zpp.mail.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;


/**
 * 测试类--填写邮件地址(ご注文予約)
 * 
 */
public class Test1 {
    public final static void main(String[] args) throws Exception {
        //新的创建httpClient的方法
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
 
            HttpPost httpPost = new HttpPost("https://aksale.advs.jp/cp/akachan_sale_pc/mail_form.cgi");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();
            nvps.add(new BasicNameValuePair("mail1", "21190096@qq.com"));
            nvps.add(new BasicNameValuePair("mail2", "21190096@qq.com"));
            nvps.add(new BasicNameValuePair("event_id", "7366872632"));
            nvps.add(new BasicNameValuePair("event_type", "5"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);
 
            try {
                System.out.println(response2.getStatusLine());
                HttpEntity entity2 = response2.getEntity();
                // do something useful with the response body
                // and ensure it is fully consumed
                EntityUtils.consume(entity2);
            } finally {
                response2.close();
            }
        } finally {
            httpclient.close();
        }

    }

}
