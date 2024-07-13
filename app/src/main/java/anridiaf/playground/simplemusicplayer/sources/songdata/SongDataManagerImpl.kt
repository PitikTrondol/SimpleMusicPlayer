package anridiaf.playground.simplemusicplayer.sources.songdata

import android.util.Log
import anridiaf.playground.simplemusicplayer.utils.ResultWrapper
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.serialization.json.Json

private const val GIST_URL = "https://api.github.com/gists/03c76ce55e73f088372e146055fc9d7e"
class SongDataManagerImpl(
    private val httpClient: HttpClient
): SongDataManager {
    override suspend fun getList(): ResultWrapper<List<SongData>> {
        return try {
            val body = httpClient.get(GIST_URL).body<GistResponse>()
            val jsonString = body.files?.playlist?.content.orEmpty()
            val resultList = Json.decodeFromString<SongDataList>(jsonString)
            val result = resultList.songData?.map { response->
                SongData(
                    thumbnail= response?.thumbnail.orEmpty(),
                    sources= response?.sources.orEmpty(),
                    artist= response?.artist.orEmpty(),
                    title= response?.title.orEmpty()
                )
            } ?: listOf()

            Log.e("AFRI", "getList : $result")

            ResultWrapper.Success(listOf())
        }catch (e: Exception){
            ResultWrapper.Error(statusResponse = e.message ?: "Exception on get song data")
        }
    }
}