package anridiaf.playground.simplemusicplayer.sources.songdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SongDataList(
	@SerialName("musics")
	val songData: List<SongData?>? = null
)

@Serializable
data class SongData(

	@SerialName("thumbnail")
	val thumbnail: String? = null,

	@SerialName("sources")
	val sources: String? = null,

	@SerialName("artist")
	val artist: String? = null,

	@SerialName("title")
	val title: String? = null
)
