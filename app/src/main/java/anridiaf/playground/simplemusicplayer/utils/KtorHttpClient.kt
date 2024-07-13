package anridiaf.playground.simplemusicplayer.utils

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.accept
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

object KtorHttpClient {

    fun createClient(): HttpClient {
        return HttpClient(OkHttp) {

            defaultRequest {
                accept(ContentType.Any)
                contentType(ContentType.Application.Json)
            }
            install(HttpTimeout) {
                this.requestTimeoutMillis = 60000
                this.connectTimeoutMillis = 60000
                this.socketTimeoutMillis = 60000
            }

            install(Logging){
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
            }

            install(ContentNegotiation){
                json(
                    Json {
                        prettyPrint = true
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }
}

