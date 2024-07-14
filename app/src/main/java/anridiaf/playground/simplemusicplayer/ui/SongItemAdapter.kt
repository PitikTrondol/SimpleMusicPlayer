package anridiaf.playground.simplemusicplayer.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorRes
import androidx.annotation.OptIn
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.recyclerview.widget.RecyclerView
import anridiaf.playground.simplemusicplayer.R
import anridiaf.playground.simplemusicplayer.databinding.PlaylistItemBinding
import coil.load

class SongItemAdapter(
    private val onItemSelected: (Int) -> Unit
) : RecyclerView.Adapter<SongItemAdapter.SongItemView>() {

    private var previousTrackSize = 0
    private var previousPlayedIndex = -1
    private var previousSelectedIndex = -1

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


    fun updateItem(item: Pair<MediaItem, Boolean>) {
        Log.e("AFRI", "selectedItem: ${item.first.mediaMetadata.title} :: play ${item.second}")

        if (previousPlayedIndex != -1) {
            mutableSongList[previousPlayedIndex] = mutableSongList[previousPlayedIndex].copy(
                isPlaying = false
            )
            notifyItemChanged(previousPlayedIndex)
        }

        val currentIndex = mutableSongList.indexOfFirst { it.mediaItem == item.first }
        previousPlayedIndex = currentIndex

        mutableSongList[currentIndex] = mutableSongList[currentIndex].copy(
            mediaItem = item.first,
            isPlaying = item.second
        )
        notifyItemChanged(currentIndex)
    }

    fun selectItem(index: Int) {
        if (previousSelectedIndex != -1) {
            mutableSongList[previousSelectedIndex] = mutableSongList[previousSelectedIndex].copy(
                bgColor = R.color.transparent
            )
            notifyItemChanged(previousSelectedIndex)
        }

        mutableSongList[index] = mutableSongList[index].copy(bgColor = R.color.platinum)
        previousSelectedIndex = index
        notifyItemChanged(index)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongItemView {
        val binding = PlaylistItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return SongItemView(binding, onItemSelected)
    }

    override fun getItemCount(): Int {
        return mutableSongList.size
    }

    override fun onBindViewHolder(holder: SongItemView, position: Int) {
        holder.update(mutableSongList[position], position)
    }

    @OptIn(UnstableApi::class)
    class SongItemView(
        private val binding: PlaylistItemBinding,
        private val onItemSelected: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun update(data: AdapterItem, position: Int) {
            val metadata = data.mediaItem.mediaMetadata
            val artworkUri = metadata.artworkUri?.path.orEmpty()
            if (artworkUri.isNotBlank()) {
                binding.songArtwork.load(metadata.artworkUri)
            } else {
                binding.songArtwork.load(R.drawable.artwork_placeholder)
            }
            binding.songSoundWave.visibility = if (data.isPlaying) View.VISIBLE else View.INVISIBLE
            binding.songTitle.text = metadata.title ?: "Title"
            binding.songArtist.text = metadata.artist ?: "Artist"
            binding.songAlbum.text = metadata.albumTitle ?: "Album"
            binding.playlistItem.setBackgroundResource(data.bgColor)
            binding.playlistItem.setOnClickListener {
                onItemSelected(position)
            }
        }
    }
}

data class AdapterItem(
    val mediaItem: MediaItem = MediaItem.EMPTY,
    val isPlaying: Boolean = false,
    @ColorRes val bgColor: Int = R.color.transparent
)