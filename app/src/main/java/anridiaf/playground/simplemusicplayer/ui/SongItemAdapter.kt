package anridiaf.playground.simplemusicplayer.ui

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
import anridiaf.playground.simplemusicplayer.R
import coil.load

class SongItemAdapter : RecyclerView.Adapter<SongItemAdapter.SongItemView>() {

    private val mutableSongList = mutableListOf<MediaItem>()

    fun updateTracks(tracks: List<MediaItem>) {
        mutableSongList.clear()
        mutableSongList.addAll(tracks)
        notifyItemRangeInserted(0, tracks.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemView {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(R.layout.playlist_item, parent, false)
        return SongItemView(view)
    }

    override fun getItemCount(): Int {
        return mutableSongList.size
    }

    override fun onBindViewHolder(holder: SongItemView, position: Int) {
        holder.update(mutableSongList[position], position)
    }

    @OptIn(UnstableApi::class)
    class SongItemView(
        private val view: View
    ) : RecyclerView.ViewHolder(view) {

        private val artwork: ImageView by lazy { view.findViewById(R.id.song_artwork) }
        private val soundWave: ImageView by lazy { view.findViewById(R.id.song_sound_wave) }
        private val title: TextView by lazy { view.findViewById(R.id.song_title) }
        private val artist: TextView by lazy { view.findViewById(R.id.song_artist) }
        private val album: TextView by lazy { view.findViewById(R.id.song_album) }

        fun update(mediaItem: MediaItem, index: Int) {
            val metadata = mediaItem.mediaMetadata
            val artworkUri = metadata.artworkUri?.path.orEmpty()
            if (artworkUri.isNotBlank()) {
                artwork.load(metadata.artworkUri)
            } else {
                artwork.load(R.drawable.artwork_placeholder)
            }
//            soundWave.visibility = if(mediaItem.) View.VISIBLE else View.INVISIBLE
            title.text = metadata.title ?: "Title"
            artist.text = metadata.artist ?: "Artist"
            album.text = metadata.albumTitle ?: "Album"
//            view.setOnClickListener { onSelectSong(mediaItem) }
        }
    }
}