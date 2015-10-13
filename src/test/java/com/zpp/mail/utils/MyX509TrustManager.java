package com.zpp.mail.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.bussness.constants.InitConstants;

/**
 * 证书信任管理器（用于https请求）
 * 注：可能由于没有实现父类方法，连接服务器时报错。
 * 
 */
public class MyX509TrustManager implements X509TrustManager {

    private static Logger log = LoggerFactory.getLogger(MyX509TrustManager.class);

    public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
    }

    public X509Certificate[] getAcceptedIssuers() {
        return null;
    }

    public static void main(String[] args) {
        String requestMethod = "GET";
//        String requestUrl = "https://210.136.104.103/cp/akachan_sale_pc/search_event_list.cgi?area2=%90_%93%de%90%ec%8c%a7&event_type=6&sid=37186&kmws=";
        String requestUrl = "https://aksale.advs.jp/cp/akachan_sale_pc/search_event_list.cgi?area2=%90_%93%de%90%ec%8c%a7&event_type=6&sid=37186&kmws=";
        String outputStr = "";
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = { new MyX509TrustManager() };
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();

            URL url = new URL(requestUrl);
            HttpsURLConnection httpsUrlConn = (HttpsURLConnection) url.openConnection();
            httpsUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
            httpsUrlConn.setRequestProperty("Accept", InitConstants.REQUEST_ACCEPT);
            httpsUrlConn.setRequestProperty("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpsUrlConn.setRequestProperty("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpsUrlConn.setRequestProperty("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpsUrlConn.setRequestProperty("Connection", InitConstants.REQUEST_CONNECTION);
            httpsUrlConn.setRequestProperty("Host", "210.136.104.103");
            httpsUrlConn.setRequestProperty("Pragma", InitConstants.REQUEST_PRAGMA);
            httpsUrlConn.setRequestProperty("Upgrade-Insecure-Requests", "1");
            httpsUrlConn.setSSLSocketFactory(ssf);
            httpsUrlConn.setHostnameVerifier(new CustomizedHostnameVerifier());

            httpsUrlConn.setDoOutput(true);
            httpsUrlConn.setDoInput(true);
            httpsUrlConn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            httpsUrlConn.setRequestMethod(requestMethod);

            if ("GET".equalsIgnoreCase(requestMethod)) {
                httpsUrlConn.connect();
            }
            // 当有数据需要提交时
            if (null != outputStr) {
                OutputStream outputStream = httpsUrlConn.getOutputStream();
                // 注意编码格式，防止中文乱码
                outputStream.write(outputStr.getBytes("Shift_JIS"));
                outputStream.close();
            }

            // 将返回的输入流转换成字符串
            InputStream inputStream = httpsUrlConn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "Shift_JIS");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            // 读取服务器的响应内容并显示
            int respInt = inputStreamReader.read();
            while (respInt != -1) {
                System.out.print((char) respInt);
                respInt = inputStreamReader.read();
            }
            

//          HttpURLConnection httpUrlConn = (HttpURLConnection) url.openConnection();
//          httpUrlConn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/44.0.2403.155 Safari/537.36");
//          httpUrlConn.setRequestProperty("Accept", InitConstants.REQUEST_ACCEPT);
//          httpUrlConn.setRequestProperty("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
//          httpUrlConn.setRequestProperty("Accept-Language", InitConstants.REQUEST_Accept_Language);
//          httpUrlConn.setRequestProperty("Cache-Control", InitConstants.REQUEST_Cache_Control);
//          httpUrlConn.setRequestProperty("Connection", InitConstants.REQUEST_CONNECTION);
//          httpUrlConn.setRequestProperty("Host", "210.136.104.103");
//          httpUrlConn.setRequestProperty("Pragma", InitConstants.REQUEST_PRAGMA);
//          httpUrlConn.setRequestProperty("Upgrade-Insecure-Requests", "1");
//
//          httpUrlConn.setDoOutput(true);
//          httpUrlConn.setDoInput(true);
//          httpUrlConn.setUseCaches(false);
//          // 设置请求方式（GET/POST）
//          httpUrlConn.setRequestMethod(requestMethod);
//
//          if ("GET".equalsIgnoreCase(requestMethod)) {
//              httpUrlConn.connect();
//          }
//          // 当有数据需要提交时
//          if (null != outputStr) {
//              OutputStream outputStream = httpUrlConn.getOutputStream();
//              // 注意编码格式，防止中文乱码
//              outputStream.write(outputStr.getBytes("Shift_JIS"));
//              outputStream.close();
//          }
//
//          // 将返回的输入流转换成字符串
//          InputStream inputStream = httpUrlConn.getInputStream();
//          InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "Shift_JIS");
//          BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//          // 读取服务器的响应内容并显示
//          int respInt = inputStreamReader.read();
//          while (respInt != -1) {
//              System.out.print((char) respInt);
//              respInt = inputStreamReader.read();
//          }
        } catch (ConnectException ce) {
            log.error("Weixin server connection timed out.");
        } catch (Exception e) {
            log.error("https request error:{}", e);
        }
    }
}
