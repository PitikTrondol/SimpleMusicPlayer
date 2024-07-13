package anridiaf.playground.simplemusicplayer.sources.songdata

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GistResponse(

	@SerialName("files")
	val files: Files? = null,

	@SerialName("url")
	val url: String? = null
)

@Serializable
data class Files(

	@SerialName("playlist")
	val playlist: Playlist? = null
)

@Serializable
data class Playlist(

	@SerialName("filename")
	val filename: String? = null,

	@SerialName("size")
	val size: Int? = null,

	@SerialName("truncated")
	val truncated: Boolean? = null,

	@SerialName("language")
	val language: String? = null,

	@SerialName("type")
	val type: String? = null,

	@SerialName("raw_url")
	val rawUrl: String? = null,

	@SerialName("content")
	val content: String? = null
)
