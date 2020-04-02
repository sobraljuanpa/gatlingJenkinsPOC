package test

import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._

class LibraryAPI extends Simulation {

	val httpProtocol = http
		.baseUrl("http://openlibrary.org")
		.inferHtmlResources()
		.acceptHeader("*/*")
		.acceptEncodingHeader("gzip, deflate")
		.userAgentHeader("PostmanRuntime/7.24.0")

	val headers_0 = Map("Postman-Token" -> "eb1a2b55-d591-442b-a299-bb96bc6fe0bc")

	val headers_1 = Map("Postman-Token" -> "83b107f8-e705-4f15-8c7f-1f2969c7ef69")

	val headers_2 = Map("Postman-Token" -> "9620f318-5e3b-468b-ba1a-d5e300386637")

    val uri2 = "http://covers.openlibrary.org/b/isbn/0385472579-S.jpg"

	val scn = scenario("LibraryAPI")
		// Search for book by title
		.exec(http("request_0")
			.get("/search.json?title=the+lord+of+the+rings")
			.headers(headers_0))
		.pause(34)
		.exec(http("request_1")
			.get("/search.json?author=tolkien")
			.headers(headers_1))
		.pause(114)
		// Look for book cover
		.exec(http("request_2")
			.get(uri2)
			.headers(headers_2))

	setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}