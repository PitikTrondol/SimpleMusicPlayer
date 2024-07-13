package anridiaf.playground.simplemusicplayer

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load

class SongItemAdapter(
    private val dataSet: List<Song>,
    private val onSelectSong: (Int)-> Unit
) : RecyclerView.Adapter<SongItemAdapter.SongItemView>() {

    private val mutableSongList = mutableListOf<Song>()

    init {
        mutableSongList.clear()
        mutableSongList.addAll(dataSet)
    }

    fun startPlaying(index: Int, song: Song) {
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
        holder.update(mutableSongList[position], position)
    }

    class SongItemView(
        private val view: View,
        private val onSelectSong: (Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {

        private val artwork: ImageView by lazy { view.findViewById(R.id.song_artwork) }
        private val soundWave: ImageView by lazy { view.findViewById(R.id.song_sound_wave) }
        private val title: TextView by lazy { view.findViewById(R.id.song_title) }
        private val artist: TextView by lazy { view.findViewById(R.id.song_artist) }
        private val album: TextView by lazy { view.findViewById(R.id.song_album) }

        fun update(newSong: Song, index: Int){
            Log.e("AFRI", "update: $newSong")
            artwork.load(R.drawable.artwork_placeholder)
            soundWave.visibility = if(newSong.isPlaying) View.VISIBLE else View.INVISIBLE
            title.text = newSong.title
            artist.text = newSong.artist
            album.text = newSong.album
            view.setOnClickListener { onSelectSong(index) }
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