package anridiaf.playground.simplemusicplayer.sources.songdata

import com.google.gson.annotations.SerializedName

data class Music(

	@field:SerializedName("files")
	val files: Files? = null,

	@field:SerializedName("url")
	val url: String? = null
)

data class Files(

	@field:SerializedName("playlist.json")
	val playlistJson: PlaylistJson? = null
)

data class PlaylistJson(

	@field:SerializedName("filename")
	val filename: String? = null,

	@field:SerializedName("size")
	val size: Int? = null,

	@field:SerializedName("truncated")
	val truncated: Boolean? = null,

	@field:SerializedName("language")
	val language: String? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("raw_url")
	val rawUrl: String? = null,

	@field:SerializedName("content")
	val content: String? = null
)
