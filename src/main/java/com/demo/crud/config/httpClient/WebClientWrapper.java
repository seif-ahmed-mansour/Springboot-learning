package com.demo.crud.config.httpClient;

import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

public class WebClientWrapper implements HttpClientWrapper {

    private final WebClient webClient;

    private WebClientWrapper(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public <T> T sendRequest(String url,
                             HttpMethod method,
                             Map<String, String> headers,
                             Map<String, String> queryParams,
                             Object body,
                             Class<T> responseType) {

        WebClient.RequestBodySpec request = webClient.method(method).uri(uriBuilder -> {
            uriBuilder.path(url);
            if (queryParams != null) {
                queryParams.forEach(uriBuilder::queryParam);
            }
            return uriBuilder.build();
        });

        if (headers != null) {
            headers.forEach(request::header);
        }

        WebClient.ResponseSpec responseSpec = (body != null)
                ? request.bodyValue(body).retrieve()
                : request.retrieve();

        return responseSpec.bodyToMono(responseType).block();
    }

    // Builder
    public static class Builder {
        private String baseUrl;

        public Builder baseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public WebClientWrapper build() {
            WebClient client = WebClient.builder()
                    .baseUrl(baseUrl != null ? baseUrl : "")
                    .build();
            return new WebClientWrapper(client);
        }
    }
}
