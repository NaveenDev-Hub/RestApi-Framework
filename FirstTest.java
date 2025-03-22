package com.example.restful.MyRestfulApp;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import io.restassured.path.json.JsonPath;
import io.restassured.parsing.Parser;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class FirstTest {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		RestAssured.baseURI = "https://api.restful-api.dev";
		
		//1. List of all objects
		 String response = given().log().all()
        .when()
            .get("/objects")
        .then()
        	.assertThat().statusCode(200)
            .body("[0].id", equalTo("1"))
            .body("[0].name", equalTo("Google Pixel 6 Pro"))
            .body("[0].data.color", equalTo("Cloudy White"))
            .body("[0].data.capacity", equalTo("128 GB"))
            .extract().response().asString();
		 System.out.println(response);
		 
		 //2. List of objects by id
		 given().log().all()
		 .when().get("/objects?id=3&id=5&id=10")
		 .then().log().all()
		 	.assertThat().statusCode(200)
		 	.body("[1].id", equalTo("5"))
		 	.body("[1].name", equalTo("Samsung Galaxy Z Fold2"))
		 	.body("[1].data.price", equalTo((float)689.99))
		 	.body("[2].data['Screen size']", equalTo((float)7.9))
		 	.extract().response().asString();
		 
		 //3.Single Object
		 given().log().all()
				 .when().get("/objects/7")
				 .then().log().all()
				 	.assertThat().statusCode(200)
				 	.body("data.year", equalTo(2019))
				 	.body("data.price", equalTo((float)1849.99))
				 	.body("data['Hard disk size']", equalTo("1 TB"))
				 	.extract().response().asString();
		
		 //4. Add object
		 String response4 =given().log().all()
			.header("Content-Type", "application/json") 
			 .body("{\n"
			 		+ "   \"name\": \"Apple MacBook Pro 16\",\n"
			 		+ "   \"data\": {\n"
			 		+ "      \"year\": 2019,\n"
			 		+ "      \"price\": 1849.99,\n"
			 		+ "      \"CPU model\": \"Naveen Intel Core i9\",\n"
			 		+ "      \"Hard disk size\": \"100 TB\"\n"
			 		+ "   }\n"
			 		+ "}")
			 .when().post("/objects")
			 .then().log().all()
		 	.assertThat().statusCode(200)
		 	.extract().asString();
		 
		 JsonPath path = new JsonPath(response4);
		 String addedId = path.getString("id");
//		 String addedId = "ff808181932badb60195bb04b5ae192e";
		 
		 //4.1 Read after post method
		 String response41 = given().log().all()
			        .when()
			            .get("/objects/" + addedId)
			        .then()
			        	.assertThat().statusCode(200)
			        	.body("name", equalTo("Apple MacBook Pro 16"))
			        	.body("data.year", equalTo(2019))
			        	.body("data.price", equalTo((float)1849.99))
			        	.body("data['Hard disk size']", equalTo("100 TB"))
			            .body("data['CPU model']", equalTo("Naveen Intel Core i9"))
			            .extract().response().asString();
					 System.out.println(response41);
		
		//5. Update Object
		given().log().all()
				.header("Content-Type", "application/json")
				.body("{\n"
						+ "   \"id\": \"" + addedId + "\",\n"
						+ "   \"name\": \"Apple MacBook Pro 16\",\n"
						+ "   \"data\": {\n"
						+ "      \"year\": 2019,\n"
						+ "      \"price\": 2049.99,\n"
						+ "      \"CPU model\": \"Sharmi Intel Core i9\",\n"
						+ "      \"Hard disk size\": \"1 TB\",\n"
						+ "      \"color\": \"silver\"\n"
						+ "   }\n"
						+ "}")
				.when()
				.put("/objects/" + addedId)
				.then().log().all().assertThat().statusCode(200);
		
		//6. Partially updating object (PATCH)
//		RestAssured.registerParser("text/plain", Parser.JSON);
		given().log().all()
			.header("Content-Type", "application/json")
			.body("{\n"
					+ "   \"id\": \"" + addedId + "\",\n"
					+ "   \"name\": \"Apple MacBook Pro 16 (Updated Name)\"\n"
					+ "}")
			.when()
			.patch("/objects/" + addedId)
			.then().log().all().assertThat().statusCode(200)
			.body("name", equalTo("Apple MacBook Pro 16 (Updated Name)"));
		
		//7.Delete object
		given().log().all()
			.header("Content-Type", "application/json")
			.when()
			.delete("/objects/" + addedId)
			.then().log().all()
			.assertThat().statusCode(200);
		
		//8.Confirm if object deleted
		given().log().all()
			.when()
			.get("/objects/" + addedId)
			.then().log().all()
			.assertThat().statusCode(404);

	}

}
