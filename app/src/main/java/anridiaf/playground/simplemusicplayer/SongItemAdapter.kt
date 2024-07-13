package anridiaf.playground.simplemusicplayer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import coil.load

class SongItemAdapter(
    private val dataSet: List<MediaItem>,
    private val onSelectSong: (MediaItem) -> Unit
) : RecyclerView.Adapter<SongItemAdapter.SongItemView>() {

    private val mutableSongList = mutableListOf<MediaItem>()

    init {
        mutableSongList.clear()
        mutableSongList.addAll(dataSet)
    }

    fun onMetadataChanged(index: Int, song: MediaItem) {
        mutableSongList[index] = song
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemView {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return SongItemView(view, onSelectSong)
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }

    override fun onBindViewHolder(holder: SongItemView, position: Int) {
        holder.update(dataSet[position], position)
    }

    @OptIn(UnstableApi::class)
    class SongItemView(
        private val view: View,
        private val onSelectSong: (MediaItem) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val artwork: ImageView by lazy { view.findViewById(R.id.song_artwork) }
        private val soundWave: ImageView by lazy { view.findViewById(R.id.song_sound_wave) }
        private val title: TextView by lazy { view.findViewById(R.id.song_title) }
        private val artist: TextView by lazy { view.findViewById(R.id.song_artist) }
        private val album: TextView by lazy { view.findViewById(R.id.song_album) }

        fun update(mediaItem: MediaItem, index: Int) {
//            Log.e("AFRI", "update: ${mediaItem.mediaMetadata}")
            val metadata = mediaItem.mediaMetadata
            if (metadata.artworkData != null) {
                artwork.load(metadata.artworkData)
            } else {
                artwork.load(R.drawable.artwork_placeholder)
            }
//            soundWave.visibility = if(mediaItem.) View.VISIBLE else View.INVISIBLE
            title.text = metadata.title ?: "Title"
            artist.text = metadata.artist ?: "Artist"
            album.text = metadata.albumTitle ?: "Album"
            view.setOnClickListener { onSelectSong(mediaItem) }
        }
    }
}

data class Song(
    val artwork: String = "",
    val title: String = "",
    val artist: String = "",
    val album: String = "",
    val isPlaying: Boolean = false
)