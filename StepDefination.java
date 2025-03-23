package stepDefinitions;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;

import static org.junit.Assert.assertEquals;

public class StepDefination {
	
	Response response;

	@Given("I entered into base URI")
	public void i_entered_into_base_uri() {
		RestAssured.baseURI = "https://api.restful-api.dev";
	}
	@When("I do GET {string}")
	public void i_do_get(String endpoint) {
		response = RestAssured
                .given().log().all()
                .when()
                .get(endpoint)
                .then().log().body()
                .extract().response();
	}
	@Then("I validate success with status code {int}")
	public void i_validate_success_with_status_code(Integer expectedStatusCode) {
		assertEquals(expectedStatusCode.intValue(), response.getStatusCode());
	}
	
}
