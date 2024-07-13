package anridiaf.playground.simplemusicplayer.utils

import io.ktor.client.HttpClient
import kotlinx.coroutines.Dispatchers
import org.koin.core.qualifier.named
import org.koin.dsl.module

val utilModule = module {

    factory<HttpClient> { KtorHttpClient.createClient() }
    single(named(ioDispatcher)) {
        Dispatchers.IO
    }

    single(named(defaultDispatcher)) {
        Dispatchers.Default
    }

    single(named(mainDispatcher)) {
        Dispatchers.Main
    }
}

const val ioDispatcher = "IODispatcher"
const val mainDispatcher = "MainDispatcher"
const val defaultDispatcher = "DefaultDispatcher"