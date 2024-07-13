package anridiaf.playground.simplemusicplayer.sources.songdata

import anridiaf.playground.simplemusicplayer.utils.ResultWrapper

interface SongDataManager {
    suspend fun getList(): ResultWrapper<List<SongData>>
}