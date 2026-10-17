package com.banking.framework.steps;

import com.banking.framework.support.CommonLib;
import com.banking.framework.support.PaymentRequestBuilder;
import com.banking.framework.client.ApiClient;
import com.banking.framework.server.DummyBankingApi;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.cucumber.java.en.Then;
import io.cucumber.java.Before;
import io.cucumber.java.After;
import org.json.JSONObject;

import java.util.Map;

import static org.junit.Assert.assertEquals;

public class CommonSteps {
    private final CommonLib commonLib = new CommonLib();
    private final PaymentRequestBuilder requestBuilder = new PaymentRequestBuilder();
    private final ApiClient apiClient = new ApiClient();
    private Map<String, String> testData;
    private int actualStatus;
    private String responseBody;

    @Before
    public void startDummyApi() throws Exception {
        DummyBankingApi.start();
    }

    @After
    public void stopDummyApi() {
        DummyBankingApi.stop();
    }

    @Given("I load payment test data {string} and {string} from worksheet {string}")
    public void loadPaymentTestData(String testCaseNo, String testDataNo, String sheetName) throws Exception {
        testData = commonLib.getPaymentData(testCaseNo, testDataNo, sheetName);
    }

    @When("I submit the payment request")
    public void submitPaymentRequest() throws Exception {
        String requestJson = requestBuilder.buildRequest(testData);
        ApiClient.ApiResponse response = apiClient.postPayment(requestJson, testData.get("Authorization"));
        actualStatus = response.statusCode();
        responseBody = response.body();
        System.out.println("\n========== API RESPONSE ==========");
        System.out.println("HTTP Status : " + actualStatus);
        System.out.println("Status      : "
                + (actualStatus >= 200 && actualStatus < 300
                        ? "OK" : "FAILED"));
        System.out.println("Message     : "
                + new JSONObject(responseBody).optString("message"));
        System.out.println("Message     : "
                + new JSONObject(responseBody).optString("message"));

        System.out.println("\nJSON Body Response:");
        System.out.println(new JSONObject(responseBody).toString(2));
        System.out.println("==================================\n");
    }

    @Then("the API response status should be {int}")
    public void validateStatus(int expectedStatus) {
        assertEquals("Unexpected HTTP status. Response: " + responseBody, expectedStatus, actualStatus);
    }

    @Then("the response code should be {string}")
    public void validateResponseCode(String expectedCode) {
        assertEquals("Unexpected response body: " + responseBody,
                expectedCode, new JSONObject(responseBody).getString("code"));
    }
}
