
Feature: Validating Restful Apis 
    I want to validate all Restful API responses including positive and negative scenarios

  Scenario Outline: Validate GET API responses with different endpoints and status codes
    Given I entered into base URI
    When I do GET "<endpoint>"
    Then I validate success with status code <statusCode>
    
    Examples:
    | endpoint								|	statusCode	|
    |	/objects								|	200					|
    |	/wrong-obj							|	404					|
    | /unauthorized-endpoint	|	401					|