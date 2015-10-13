/*
 * ====================================================================
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 *
 */
package com.zpp.mail.sample;

import java.io.File;
import java.io.FileInputStream;
import java.security.KeyStore;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;

import com.zpp.mail.bussness.constants.InitConstants;

/**
 * 官网下载的SSL例子 http://hc.apache.org/httpcomponents-client-4.5.x/examples.html
 * This example demonstrates how to create secure connections with a custom SSL
 * context.
 */
public class ClientCustomSSL {

    public final static void main(String[] args) throws Exception {
        SSLContext sslcontext = SSLContexts.custom()
                .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        
        // Trust own CA and all self-signed certs
//        SSLContext sslcontext = SSLContexts.custom()
//                .loadTrustMaterial(new File("E:/projects/workspace_hadoop/advc_new/src/test/java/weiqz.keystore"), "123456".toCharArray(),
//                        new TrustSelfSignedStrategy())
//                .build();
        
        
        
        // Allow TLSv1 protocol only
//        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
//                sslcontext,
//                new String[] { "TLSv1" },
//                null,
//                SSLConnectionSocketFactory.getDefaultHostnameVerifier());

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslcontext, NoopHostnameVerifier.INSTANCE);
        CloseableHttpClient httpclient = HttpClients.custom()
                .setSSLSocketFactory(sslsf)
                .build();
        try {

            HttpGet httpget = new HttpGet("https://210.136.104.103/cp/akachan_sale_pc/search_event_list.cgi?area2=%90_%93%de%90%ec%8c%a7&event_type=6&sid=37186&kmws=");
//            HttpGet httpget = new HttpGet("https://aksale.advs.jp/cp/akachan_sale_pc/search_event_list.cgi?area2=%90_%93%de%90%ec%8c%a7&event_type=6&sid=37186&kmws=");
            httpget.setHeader("User-Agent", InitConstants.REQUEST_AGENT);
            httpget.setHeader("Accept", InitConstants.REQUEST_ACCEPT);
            httpget.setHeader("Accept-Encoding", InitConstants.REQUEST_ACCEPT_ENCODING);
            httpget.setHeader("Accept-Language", InitConstants.REQUEST_Accept_Language);
            httpget.setHeader("Cache-Control", InitConstants.REQUEST_Cache_Control);
            httpget.setHeader("Connection", InitConstants.REQUEST_CONNECTION);
            httpget.setHeader("Host", InitConstants.REQUEST_HOST);
            httpget.setHeader("Pragma", InitConstants.REQUEST_PRAGMA);

            System.out.println("executing request " + httpget.getRequestLine());

            CloseableHttpResponse response = httpclient.execute(httpget);
            try {
                HttpEntity entity = response.getEntity();

                System.out.println("----------------------------------------");
                System.out.println(response.getStatusLine());
                EntityUtils.consume(entity);
            } finally {
                response.close();
            }
        } finally {
            httpclient.close();
        }
        
//      // Trust own CA and all self-signed certs
//      sslcontext = SSLContexts.custom()
//              .loadTrustMaterial(new File("E:/projects/workspace_hadoop/advc_new/src/test/java/weiqz.keystore"), "123456".toCharArray(),
//                      new TrustSelfSignedStrategy())
//              .build();
//      // Allow TLSv1 protocol only
//      sslsf = new SSLConnectionSocketFactory(
//              sslcontext,
//              new String[] { "TLSv1" },
//              null,
//              SSLConnectionSocketFactory.getDefaultHostnameVerifier());
//      CloseableHttpClient httpclient = HttpClients.custom()
//              .setSSLSocketFactory(sslsf)
//              .build();
    }

}
