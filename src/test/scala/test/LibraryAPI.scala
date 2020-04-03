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

	val feeder = Array(
		Map("bookName"->"the lord of the rings"),
		Map("bookName"->"fear and loathing in las vegas"),
		Map("bookName"->"the catcher in the rye"),
		Map("bookName"->"the shining")
	).random

	object BookSearch {
		val getBook = exec (http("Paso 1 - Buscar libro")
			.get("/search.json")
			.queryParam("title", "${bookName}")
			.headers(headers_0)
			.check(jsonPath("$..author_name[0]").ofType[String].saveAs("authorName"))
		)
		.pause(1)
	}

	object AuthorSearch {
		val getAuthor = exec(http("Paso 2 - Buscar autor")
			.get("/search.json?author=${authorName}")
			.headers(headers_1)
			.check(jsonPath("$..cover_i").find(0).saveAs("coverID"))//.ofType[Int]
		)
		.pause(1)
	}

	object CoverSearch {
		val getCover = exec(http("Paso 3 - Buscar Tapa del libro")
			.get("http://covers.openlibrary.org/b/ID/${coverID}-L.jpg")
			.headers(headers_2)
		)
	}

	val scn = scenario("LibraryAPI")
		.feed(feeder)
		.exec(BookSearch.getBook, AuthorSearch.getAuthor, CoverSearch.getCover)

	setUp(scn.inject(atOnceUsers(4))).protocols(httpProtocol)
}