package anridiaf.playground.simplemusicplayer.sources

import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManagerImpl
import anridiaf.playground.simplemusicplayer.utils.ioDispatcher
import io.ktor.client.HttpClient
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.core.qualifier.named
import org.koin.dsl.module

val dataSourceModule = module {
    factory<SongDataManager> {
        SongDataManagerImpl(
            httpClient = get<HttpClient>(),
            dispatcher = get<CoroutineDispatcher>(qualifier = named(ioDispatcher))
        )
    }
}