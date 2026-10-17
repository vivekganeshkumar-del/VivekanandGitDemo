package com.banking.framework.support;

import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

public class PaymentRequestBuilder {
    private static final Path TEMPLATE = Path.of("src/test/resources/requests/payment-request.json");

    public String buildRequest(Map<String, String> data) throws Exception {
        String template = Files.readString(TEMPLATE);
        // Replace string placeholders first.
        template = template.replace("\"MSGIDXXXX\"", JSONObject.quote(data.get("messageId")))
                .replace("\"DEBTORXXXX\"", JSONObject.quote(data.get("debtor")))
                .replace("\"CREDITORXXXX\"", JSONObject.quote(data.get("creditor")))
                .replace("\"CCYXXXX\"", JSONObject.quote(data.get("currency")))
                .replace("\"AUTHTOKENXXXX\"", JSONObject.quote(data.get("Authorization")))
                .replace("AMOUNTXXXX", data.get("amount"));
        return new JSONObject(template).toString();
    }
}
