import okhttp3.*
import org.junit.jupiter.api.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.example.ModelUser
import kotlin.test.assertEquals


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class Test__crudPetUser {

    private val client = OkHttpClient()
    private val mapper = jacksonObjectMapper()
    private val baseUrl = "https://petstore.swagger.io/v2/user"
    private val username = "Alex"

    private val user = ModelUser.User(
        id = 123456789,
        username = username,
        firstName = "Alex",
        lastName = "Nazar",
        email = "alex@mail.com",
        password = "12345",
        phone = "+79998887766",
        userStatus = 1
    )

    @Test
    fun crudUser() {
        // CREATE
        val createResponse = post("$baseUrl", user)
        assertEquals(200, createResponse.code, "Создание пользователя должно вернуть 200")

        Thread.sleep(10000)

        // DELETE
        val deleteResponse = delete("$baseUrl/$username")

        Thread.sleep(10000)

        // Проверка, что пользователь удалён
        val afterDelete = delete("$baseUrl/$username")
        assertEquals(404, afterDelete.code, "После удаления должен быть 404")
    }

    // HTTP methods

    private fun post(url: String, bodyObj: Any): Response {
        val json = mapper.writeValueAsString(bodyObj)
        val body = json.toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(url)
            .post(body)
            .addHeader("Content-Type", "application/json")
            .addHeader("accept", "application/json")
            .build()
        return client.newCall(request).execute()
    }

    private fun delete(url: String): Response {
        val request = Request.Builder()
            .url(url)
            .delete()
            .addHeader("accept", "application/json")
            .build()
        return client.newCall(request).execute()
    }
}