package com.demo.crud.config.httpClient;

import org.springframework.http.HttpMethod;
import java.util.Map;

public interface HttpClientWrapper {
    <T> T sendRequest(
            String url,
            HttpMethod method,
            Map<String, String> headers,
            Map<String, String> queryParams,
            Object body,
            Class<T> responseType
    );
}
