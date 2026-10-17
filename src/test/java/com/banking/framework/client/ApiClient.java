package com.banking.framework.client;

/** API-facing client. Delegates the HTTP mechanics to HttpClientHelper. */
public class ApiClient {
    private final HttpClientHelper httpClientHelper = new HttpClientHelper();

    public ApiResponse postPayment(String requestJson, String authorization) throws Exception {
        return httpClientHelper.post("http://localhost:8089/api/v1/payments", requestJson, authorization);
    }

    public record ApiResponse(int statusCode, String body) {}
}
