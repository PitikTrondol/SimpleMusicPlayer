package anridiaf.playground.simplemusicplayer.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import anridiaf.playground.simplemusicplayer.R
import anridiaf.playground.simplemusicplayer.databinding.PlaylistItemBinding
import coil.load

class SongItemAdapter : RecyclerView.Adapter<SongItemAdapter.SongItemView>() {

    private var previousTrackSize = 0
    private val mutableSongList = mutableListOf<AdapterItem>()

    fun updateTracks(tracks: List<MediaItem>) {
        mutableSongList.clear()
        notifyItemRangeRemoved(0, previousTrackSize)

        mutableSongList.addAll(
            tracks.map {
                AdapterItem(mediaItem = it)
            }
        )
        notifyItemRangeInserted(0, tracks.size)
        previousTrackSize = tracks.size
    }

    private var previousIndex = -1
    fun playSong(item: Pair<MediaItem, Boolean>) {
        Log.e("AFRI", "playSong: ${item.first.mediaMetadata.title} :: ${item.second}")

        if(previousIndex != -1){
            mutableSongList[previousIndex] = mutableSongList[previousIndex].copy(
                isPlaying = false
            )
            notifyItemChanged(previousIndex)
        }

        val currentIndex = mutableSongList.indexOfFirst { it.mediaItem == item.first }
        previousIndex = currentIndex

        mutableSongList[currentIndex] = mutableSongList[currentIndex].copy(
            mediaItem = item.first,
            isPlaying = item.second
        )
        notifyItemChanged(currentIndex)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemView {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
//        val view = LayoutInflater
//            .from(parent.context)
//            .inflate(R.layout.playlist_item, parent, false)
        return SongItemView(binding)
    }

    override fun getItemCount(): Int {
        return mutableSongList.size
    }

    override fun onBindViewHolder(holder: SongItemView, position: Int) {
        holder.update(mutableSongList[position])
    }

    @OptIn(UnstableApi::class)
    class SongItemView(
        private val binding: PlaylistItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun update(data: AdapterItem) {
            val metadata = data.mediaItem.mediaMetadata
            val artworkUri = metadata.artworkUri?.path.orEmpty()
            if (artworkUri.isNotBlank()) {
                binding.songArtwork.load(metadata.artworkUri)
            } else {
                binding.songArtwork.load(R.drawable.artwork_placeholder)
            }
            binding.songSoundWave.visibility = if(data.isPlaying) View.VISIBLE else View.INVISIBLE
            binding.songTitle.text = metadata.title ?: "Title"
            binding.songArtist.text = metadata.artist ?: "Artist"
            binding.songAlbum.text = metadata.albumTitle ?: "Album"
//            view.setOnClickListener { onSelectSong(mediaItem) }
        }
    }
}

data class AdapterItem(
    val mediaItem: MediaItem = MediaItem.EMPTY,
    val isPlaying: Boolean = false
)