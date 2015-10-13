package com.zpp.mail.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 测试类--请求发送邮件(ご注文予約)
 * 
 */
public class Test2 {
    private static final Logger log = LoggerFactory.getLogger(Test2.class);

    public final static void main(String[] args) throws Exception {
        //新的创建httpClient的方法
        CloseableHttpClient httpclient = HttpClients.createDefault();
        try {
 
//            HttpPost httpPost = new HttpPost("http://www.baidu.com");
            HttpPost httpPost = new HttpPost("https://aksale.advs.jp/cp/akachan_sale_pc/mail_confirm.cgi");
//            httpPost.setHeader("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0)");
            httpPost.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.152 Safari/537.36");
            httpPost.setHeader("Referer", "https://aksale.advs.jp/cp/akachan_sale_pc/mail_form.cgi");

            List <NameValuePair> nvps = new ArrayList <NameValuePair>();

            // 邮箱地址转码 21190096@qq.com---->21190096%40qq%2ecom
            // 既@-->%40
            // 既.-->%2e
            nvps.add(new BasicNameValuePair("mail1", "taziqe%40yahoo%2eco%2ejp"));
            nvps.add(new BasicNameValuePair("mail2", "taziqe%40yahoo%2eco%2ejp"));
            nvps.add(new BasicNameValuePair("event_id", "8806698489"));
            nvps.add(new BasicNameValuePair("event_type", "5"));
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
            CloseableHttpResponse response2 = httpclient.execute(httpPost);

            // 打印http响应
            HttpEntity httpEntity =response2.getEntity();
            BufferedReader br = new BufferedReader(new InputStreamReader(httpEntity.getContent(), "Shift_JIS"));
            String line;
            while((line = br.readLine()) != null){
                log.debug(line);
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
        } finally {
            httpclient.close();
        }

    }

}
