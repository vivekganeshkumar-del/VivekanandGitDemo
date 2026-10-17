# Banking Payments API Automation Framework (without ExcelReader.java)

This project intentionally has **no `ExcelReader.java`**. Excel access is handled inside `CommonLib.java` with Fillo, following the design visible in your screenshots.

## Framework flow
`payment.feature` → `TestRunner.java` → `CommonSteps.java` → `CommonLib.java` (Fillo `Recordset`) → `PaymentRequestBuilder.java` → `ApiClient.java` → `HttpClientHelper.java` → local `DummyBankingApi.java` → assertions.

## Requirements
- JDK 17 or later (JDK 25 is fine)
- Maven 3.9+
- Internet access for the first Maven dependency download

## Run
From the extracted project directory:
```bash
mvn clean test
```
The test hooks start the local dummy API at `http://localhost:8089/api/v1/payments` before the scenarios and stop it afterward. No real bank is contacted.

## Excel data
`src/test/resources/TestData.xlsx` has two worksheets:
- `positive`
- `negative`

Each row is selected by `TC_NO` and `TD_NO`. `CommonLib.getData(...)` returns a Fillo `Recordset`; `CommonLib.getPaymentData(...)` converts the matching row into a map. No separate Excel reader class is used.

## Test coverage
- Valid payment: expected HTTP 201
- Zero amount: expected HTTP 400
- Missing currency: expected HTTP 400
- Invalid bearer token: expected HTTP 401

## Important
This is a clean, runnable-style recreation based on the screenshots and requested architecture, not a byte-for-byte copy of your original internal project. The dummy endpoint is only for learning/testing.
