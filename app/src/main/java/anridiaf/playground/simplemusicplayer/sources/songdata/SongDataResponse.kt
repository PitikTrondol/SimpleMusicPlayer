package anridiaf.playground.simplemusicplayer.sources.songdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongDataList(
	@SerialName("musics")
	val songData: List<SongDataResponse?>? = null
)

@Serializable
data class SongDataResponse(

	@SerialName("thumbnail")
	val thumbnail: String? = null,

	@SerialName("sources")
	val sources: String? = null,

	@SerialName("artist")
	val artist: String? = null,

	@SerialName("title")
	val title: String? = null,

	@SerialName("album")
	val album: String? = null
)

data class SongData(
	val thumbnail: String,
	val sources: String,
	val artist: String,
	val title: String,
	val album: String
)