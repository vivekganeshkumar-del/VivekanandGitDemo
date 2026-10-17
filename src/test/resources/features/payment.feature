@payments
Feature: Banking payment API validation using Excel test data

  @positive
  Scenario Outline: Submit a valid payment
    Given I load payment test data "<TC_NO>" and "<TD_NO>" from worksheet "<SHEET>"
    When I submit the payment request
    Then the API response status should be <EXPECTED_STATUS>
    And the response code should be "<EXPECTED_CODE>"

    Examples:
      | TC_NO  | TD_NO  | SHEET    | EXPECTED_STATUS | EXPECTED_CODE |
      | TC_001 | TD_001 | positive | 201             | PAYMENT_CREATED |
      | TC_002 | TD_002 | positive | 201             | PAYMENT_CREATED |

  @negative
  Scenario Outline: Reject invalid payment data
    Given I load payment test data "<TC_NO>" and "<TD_NO>" from worksheet "<SHEET>"
    When I submit the payment request
    Then the API response status should be <EXPECTED_STATUS>
    And the response code should be "<EXPECTED_CODE>"

    Examples:
      | TC_NO  | TD_NO  | SHEET    | EXPECTED_STATUS | EXPECTED_CODE       |
      | TC_101 | TD_101 | negative | 400             | INVALID_AMOUNT      |
      | TC_102 | TD_102 | negative | 400             | CURRENCY_REQUIRED   |
      | TC_103 | TD_103 | negative | 401             | UNAUTHORIZED        |
