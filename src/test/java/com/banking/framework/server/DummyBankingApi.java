package com.banking.framework.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import org.json.JSONObject;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Executors;

public final class DummyBankingApi {
    private static HttpServer server;
    private DummyBankingApi() {}

    public static synchronized void start() throws IOException {
        if (server != null) return;
        server = HttpServer.create(new InetSocketAddress("localhost", 8089), 0);
        server.createContext("/api/v1/payments", DummyBankingApi::handlePayment);
        server.setExecutor(Executors.newCachedThreadPool());
        server.start();
        System.out.println("Dummy banking API started at http://localhost:8089/api/v1/payments");
    }

    public static synchronized void stop() {
        if (server != null) {
            server.stop(0);
            server = null;
            System.out.println("Dummy banking API stopped.");
        }
    }

    private static void handlePayment(HttpExchange exchange) throws IOException {
        if (!"POST".equalsIgnoreCase(exchange.getRequestMethod())) {
            send(exchange, 405, "METHOD_NOT_ALLOWED", "Only POST is supported");
            return;
        }

        String auth = exchange.getRequestHeaders().getFirst("Authorization");
        if (auth == null || !auth.equals("Bearer valid-test-token")) {
            send(exchange, 401, "UNAUTHORIZED", "Missing or invalid test token");
            return;
        }

        String requestBody = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        try {
            JSONObject request = new JSONObject(requestBody);
            if (!request.has("amount") || request.optDouble("amount", 0) <= 0) {
                send(exchange, 400, "INVALID_AMOUNT", "Amount must be greater than zero");
                return;
            }
            if (request.optString("currency").isBlank()) {
                send(exchange, 400, "CURRENCY_REQUIRED", "Currency is required");
                return;
            }
            JSONObject body = new JSONObject()
                    .put("code", "PAYMENT_CREATED")
                    .put("message", "Dummy payment accepted")
                    .put("messageId", request.optString("messageId"))
                    .put("paymentStatus", "ACCEPTED");
            sendRaw(exchange, 201, body.toString());
        } catch (Exception e) {
            send(exchange, 400, "INVALID_REQUEST", "Request body is not valid JSON");
        }
    }

    private static void send(HttpExchange exchange, int status, String code, String message) throws IOException {
        sendRaw(exchange, status, new JSONObject().put("code", code).put("message", message).toString());
    }

    private static void sendRaw(HttpExchange exchange, int status, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(status, bytes.length);
        try (var output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
