package com.demo.crud.config.httpClient;
import okhttp3.*;
import org.springframework.http.HttpMethod;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;
import static org.springframework.http.HttpMethod.*;




public class OkHttpClientWrapper implements HttpClientWrapper {

    private final OkHttpClient client;
    private final ObjectMapper mapper = new ObjectMapper();

    private OkHttpClientWrapper(OkHttpClient client) {
        this.client = client;
    }

    @Override
    public <T> T sendRequest(String url,
                             HttpMethod method,
                             Map<String, String> headers,
                             Map<String, String> queryParams,
                             Object body,
                             Class<T> responseType) {

        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        if (queryParams != null) {
            queryParams.forEach(urlBuilder::addQueryParameter);
        }
        String finalUrl = urlBuilder.build().toString();

        RequestBody requestBody = null;
        if (body != null && method != GET) {
            try {
                String json = mapper.writeValueAsString(body);
                requestBody = RequestBody.create(json, MediaType.parse("application/json"));
            } catch (Exception e) {
                throw new RuntimeException("Failed to serialize body", e);
            }
        }

        Request.Builder requestBuilder = new Request.Builder()
                .url(finalUrl);

        if (headers != null) {
            headers.forEach(requestBuilder::addHeader);
        }

        if (method == GET) {
            requestBuilder = requestBuilder.get();
        } else if (method == POST) {
            requestBuilder = requestBuilder.post(requestBody);
        } else if (method == PUT) {
            requestBuilder = requestBuilder.put(requestBody);
        } else if (method == DELETE) {
            requestBuilder = requestBuilder.delete();
        } else {
            throw new IllegalArgumentException("Unsupported HTTP method: " + method);
        }

        try (Response response = client.newCall(requestBuilder.build()).execute()) {
            if (!response.isSuccessful()) {
                String errorBody = response.body() != null ? response.body().string() : "null";
                throw new RuntimeException(
                        "Request failed with code: " + response.code() + " and body: " + errorBody
                );
            }

            String responseBody = response.body() != null ? response.body().string() : null;

            return mapper.readValue(responseBody, responseType);
        } catch (IOException e) {
            throw new RuntimeException("OkHttp request failed: " + e.getMessage(), e);
        }

    }

    public static class Builder {
        private int timeoutSeconds = 30;
        private boolean sslValidationEnabled = true;

        public Builder timeout(int seconds) {
            this.timeoutSeconds = seconds;
            return this;
        }

        public Builder sslValidationEnabled(boolean enabled) {
            this.sslValidationEnabled = enabled;
            return this;
        }

        public OkHttpClientWrapper build() {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .callTimeout(java.time.Duration.ofSeconds(timeoutSeconds));

            if (!sslValidationEnabled) {
                try {
                    TrustManager[] trustAllCerts = new TrustManager[]{
                            new X509TrustManager() {
                                public void checkClientTrusted(X509Certificate[] chain, String authType) {}
                                public void checkServerTrusted(X509Certificate[] chain, String authType) {}
                                public X509Certificate[] getAcceptedIssuers() { return new X509Certificate[0]; }
                            }
                    };

                    SSLContext sslContext = SSLContext.getInstance("SSL");
                    sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

                    SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();

                    clientBuilder.sslSocketFactory(sslSocketFactory, (X509TrustManager) trustAllCerts[0]);
                    clientBuilder.hostnameVerifier((hostname, session) -> true); // skip hostname check
                } catch (Exception e) {
                    throw new RuntimeException("Failed to disable SSL validation", e);
                }
            }

            return new OkHttpClientWrapper(clientBuilder.build());
        }
    }

}