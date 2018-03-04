package com.ly.douban.support.utils;


import android.util.Log;

import java.io.EOFException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

public class OkHttpUtils {

    private static OkHttpClient okHttpClient;

    static int HTTP_CONNECT_TIMEOUT = 10 * 1000;

    public static OkHttpClient getInstance() {
        if (okHttpClient == null) {
            synchronized (OkHttpUtils.class) {
                if (okHttpClient == null) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder();
                    builder.connectTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                    builder.readTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                    builder.writeTimeout(HTTP_CONNECT_TIMEOUT, TimeUnit.MILLISECONDS);
                    builder.protocols(Collections.singletonList(Protocol.HTTP_1_1));
                    setSSL(builder);
                    builder.addInterceptor(getIns());
                    okHttpClient = builder.build();
                }
            }
        }
        return okHttpClient;
    }

    public static void setSSL(OkHttpClient.Builder builder) {

        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
            @Override
            public void checkClientTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public void checkServerTrusted(
                    X509Certificate[] chain,
                    String authType) throws CertificateException {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return new X509Certificate[]{};
            }
        }};
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts,
                    new java.security.SecureRandom());
            SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
            builder.sslSocketFactory(sslSocketFactory);
            HttpsURLConnection.setDefaultSSLSocketFactory(sslSocketFactory);
        } catch (Exception e) {
            e.printStackTrace();
        }
        HostnameVerifier hostnameVerifier = new HostnameVerifier() {

            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        };
        HttpsURLConnection.setDefaultHostnameVerifier(hostnameVerifier);
        builder.hostnameVerifier(hostnameVerifier);

    }

    private static Interceptor getIns() {
        Interceptor interceptor = new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {


                Request request = chain.request();
                Response response = null;
                try {
                    response = chain.proceed(request);
                } catch (IOException e) {
                    return response;
                }
                ResponseBody responseBody = response.body();
                long contentLength = responseBody.contentLength();

                if (!bodyEncoded(response.headers())) {
                    BufferedSource source = responseBody.source();
                    source.request(Long.MAX_VALUE); // Buffer the entire body.
                    Buffer buffer = source.buffer();

                    Charset charset = UTF8;
                    MediaType contentType = responseBody.contentType();
                    if (contentType != null) {
                        try {
                            charset = contentType.charset(UTF8);
                        } catch (UnsupportedCharsetException e) {
                            return response;
                        }
                    }

                    if (!isPlaintext(buffer)) {
                        return response;
                    }

                    if (contentLength != 0) {
                        String result = buffer.clone().readString(charset);
                        Log.e("test"," response.url():" + response.request().url());
                        Log.e("test"," response.body():" + result);
                        //得到所需的string，开始判断是否异常
                        //***********************do something*****************************

                    }

                }


                return response;
            }
        };
        return interceptor;
    }


    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static boolean bodyEncoded(Headers headers) {
        String contentEncoding = headers.get("Content-Encoding");
        return contentEncoding != null && !contentEncoding.equalsIgnoreCase("identity");
    }

    static boolean isPlaintext(Buffer buffer) throws EOFException {
        try {
            Buffer prefix = new Buffer();
            long byteCount = buffer.size() < 64 ? buffer.size() : 64;
            buffer.copyTo(prefix, 0, byteCount);
            for (int i = 0; i < 16; i++) {
                if (prefix.exhausted()) {
                    break;
                }
                int codePoint = prefix.readUtf8CodePoint();
                if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                    return false;
                }
            }
            return true;
        } catch (EOFException e) {
            return false; // Truncated UTF-8 sequence.
        }
    }
}