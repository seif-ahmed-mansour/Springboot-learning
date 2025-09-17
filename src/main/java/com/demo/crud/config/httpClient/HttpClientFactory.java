package com.demo.crud.config.httpClient;
public class HttpClientFactory {
    public enum ClientType { WEBCLIENT, OKHTTP }

    public static HttpClientWrapper create(ClientType type) {
        return switch (type) {
            case WEBCLIENT -> new WebClientWrapper.Builder().build();
            case OKHTTP -> new OkHttpClientWrapper.Builder().build();
        };
    }
}

