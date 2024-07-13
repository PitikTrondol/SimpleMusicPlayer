package anridiaf.playground.simplemusicplayer.utils

import io.ktor.client.HttpClient
import org.koin.dsl.module

val utilModule = module {

    factory<HttpClient> { KtorHttpClient.createClient() }
}