package anridiaf.playground.simplemusicplayer.sources

import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManagerImpl
import io.ktor.client.HttpClient
import org.koin.dsl.module

val dataSourceModule = module {
    factory<SongDataManager> {
        SongDataManagerImpl(
            httpClient = get<HttpClient>()
        )
    }
}