package test

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class RecipesAPI extends Simulation {

	val httpProtocol = http
		.baseUrl("http://recipepuppy.com/api")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("PostmanRuntime/7.22.0")

	val headers_0 = Map("Postman-Token" -> "b81c96a8-17b0-4d5a-aefd-d50a7b26a551")

	val headers_1 = Map("Postman-Token" -> "faa1c4f0-1909-458a-9374-3c756b89f3bf")

	val feeder = Array(
		Map("ingredient"->"potato"),
		Map("ingredient"->"onion"),
		Map("ingredient"->"garlic"),
		Map("ingredient"->"tomato")
	).random

	object Home {
		val apiBaseURL = exec(http("Paso1_Base")
			.get("/")
			.headers(headers_0))
			.pause(1)
	}

	object Search {
		val searchRecipes = exec(http("Paso2_Busqueda")
			.get("/")
			.queryParam("i", "${ingredient}")
			.headers(headers_1))
			.pause(1)
	}

	val scn = scenario("RecipesAPI")
		.feed(feeder)
		.exec(Home.apiBaseURL, Search.searchRecipes)

	setUp(scn.inject(rampUsers(5) during (10 seconds)))
		.protocols(httpProtocol)
		.assertions(global.successfulRequests.percent.is(100))
}