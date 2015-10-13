package com.zpp.mail.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.SSLContext;

import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ProtocolException;
import org.apache.http.client.CircularRedirectException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpHead;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.client.utils.URIUtils;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultRedirectStrategy;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.RedirectLocations;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.Args;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zpp.mail.bussness.constants.InitConstants;

public class HttpClientUtil {
    private static final Logger log = LoggerFactory.getLogger(HttpClientUtil.class);
    public static CloseableHttpClient getNewHttpClient() {
        HttpClientBuilder httpBuilder = HttpClientBuilder.create();
        try { 
            SSLContext sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
            SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext, NoopHostnameVerifier.INSTANCE);

            httpBuilder.setSSLSocketFactory(sslsf);
            // 重定向 参考 http://stackoverflow.com/questions/3658721/httpclient-4-error-302-how-to-redirect
            httpBuilder.setRedirectStrategy(new DefaultRedirectStrategy() {                
                public boolean isRedirected(HttpRequest request, HttpResponse response, HttpContext context)  {
                    boolean isRedirect=false;
                    try {
                        isRedirect = super.isRedirected(request, response, context);
                    } catch (ProtocolException e) {
                        e.printStackTrace();
                    }
                    if (!isRedirect) {
                        int responseCode = response.getStatusLine().getStatusCode();
                        if (responseCode == 301 || responseCode == 302) {
                            return true;
                        }
                    }
                    return isRedirect;
                }
                public URI getLocationURI(
                        final HttpRequest request,
                        final HttpResponse response,
                        final HttpContext context) throws ProtocolException {
                    Args.notNull(request, "HTTP request");
                    Args.notNull(response, "HTTP response");
                    Args.notNull(context, "HTTP context");

                    final HttpClientContext clientContext = HttpClientContext.adapt(context);

                    //get the location header to find out where to redirect to
                    final Header locationHeader = response.getFirstHeader("location");
                    if (locationHeader == null) {
                        // got a redirect response, but no location header
                        throw new ProtocolException(
                                "Received redirect response " + response.getStatusLine()
                                + " but no location header");
                    }
                    String location = locationHeader.getValue();
                    if (log.isDebugEnabled()) {
                        log.debug("Redirect requested to location '" + location + "'");
                    }
                    location = location.replaceAll("aksale\\.advs\\.jp", InitConstants.HANDLE3_REDIRECT);

                    final RequestConfig config = clientContext.getRequestConfig();

                    URI uri = createLocationURI(location);

                    // rfc2616 demands the location value be a complete URI
                    // Location       = "Location" ":" absoluteURI
                    try {
                        if (!uri.isAbsolute()) {
                            if (!config.isRelativeRedirectsAllowed()) {
                                throw new ProtocolException("Relative redirect location '"
                                        + uri + "' not allowed");
                            }
                            // Adjust location URI
                            final HttpHost target = clientContext.getTargetHost();
                            Asserts.notNull(target, "Target host");
                            final URI requestURI = new URI(request.getRequestLine().getUri());
                            final URI absoluteRequestURI = URIUtils.rewriteURI(requestURI, target, false);
                            uri = URIUtils.resolve(absoluteRequestURI, uri);
                        }
                    } catch (final URISyntaxException ex) {
                        throw new ProtocolException(ex.getMessage(), ex);
                    }

                    RedirectLocations redirectLocations = (RedirectLocations) clientContext.getAttribute(
                            HttpClientContext.REDIRECT_LOCATIONS);
                    if (redirectLocations == null) {
                        redirectLocations = new RedirectLocations();
                        context.setAttribute(HttpClientContext.REDIRECT_LOCATIONS, redirectLocations);
                    }
                    if (!config.isCircularRedirectsAllowed()) {
                        if (redirectLocations.contains(uri)) {
                            throw new CircularRedirectException("Circular redirect to '" + uri + "'");
                        }
                    }
                    redirectLocations.add(uri);
                    return uri;
                }
            });

        } catch (Exception e) { 
            log.error(e.getMessage(), e);
        }
        return httpBuilder.build();
     }
    
    
    public static void main(String[] args) {
        System.out.println("before");

        InitPropertiesUtil.getInstance(InitConstants.INIT_FILE_PATH);
        HttpClientBuilder httpBuilder = HttpClients.custom();

        SSLContext sslContext = null;
        try {
            // 参考/advc_new/src/test/java/com/zpp/mail/sample/ClientCustomSSL.java
            sslContext = SSLContexts.custom()
                    .loadTrustMaterial(null, new TrustSelfSignedStrategy()).build();
        } catch (KeyManagementException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (KeyStoreException e) {
            e.printStackTrace();
        }

        SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(sslContext,NoopHostnameVerifier.INSTANCE);
        httpBuilder.setSSLSocketFactory(sslsf);

        httpBuilder.build();
        System.out.println("after");
    }
}
