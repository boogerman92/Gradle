import io.restassured.RestAssured
import io.restassured.filter.log.RequestLoggingFilter
import io.restassured.filter.log.ResponseLoggingFilter
import io.restassured.path.json.JsonPath
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.data.Percentage
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test


class Test__crudPetUser {

    @Test
    @DisplayName("Проверка количества подсказок в ЕМ РУ")
    fun test_hintCountRusEm() {
        // включаем постоянное логирование запросов и ответов
        RestAssured.filters(RequestLoggingFilter(), ResponseLoggingFilter())

        val indexUrl = "http://dmz-es-suggestion.search.prod.aservices.tech:80/prod_expert_suggestion_read/_count"
        val expectedCount = 9834844

        val response = RestAssured
            .given()
            .header("x-source", "qa/simple-test")
            .get(indexUrl)
            .then()
            .statusCode(200)
            .extract()
            .asString()

        val actualCount = JsonPath.from(response).getInt("count")

        println("Actual count: $actualCount")

        assertThat(actualCount)
            .withFailMessage("Количество подсказок не в пределах 5% от ожидаемого")
            .isCloseTo(expectedCount, Percentage.withPercentage(5.0))
    }
}