package anridiaf.playground.simplemusicplayer

import android.content.ContentResolver
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.Tracks
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.session.MediaController
import androidx.media3.session.MediaSession
import androidx.recyclerview.widget.LinearLayoutManager
import anridiaf.playground.simplemusicplayer.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val exoPlayer : ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val session : MediaSession by lazy { MediaSession.Builder(requireContext(), exoPlayer).build() }
    private val controller by lazy {
        MediaController.Builder(requireContext(), session.token).buildAsync()
    }
    private val uri: Uri = Uri.Builder()
        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
        .path(R.raw.pedro.toString())
        .build()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(UnstableApi::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val itemList = dummySongs.map {
            MediaItem.fromUri(uri)
        }
        val adapter = SongItemAdapter(dummySongs){index->
            exoPlayer.setMediaItems(
                itemList,
                index,
                C.TIME_UNSET
            )
            exoPlayer.prepare()
            binding.controller.visibility = View.VISIBLE
        }

        binding.controller.visibility = View.INVISIBLE
        binding.controller.player = exoPlayer
        binding.playlist.layoutManager = LinearLayoutManager(requireContext())
        binding.playlist.adapter = adapter

        exoPlayer.addListener(
            object : Player.Listener{
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    Log.e("AFRI", "onIsPlayingChanged: ")
                    super.onIsPlayingChanged(isPlaying)
                    adapter.startPlaying(
                        exoPlayer.currentMediaItemIndex,
                        dummySongs[exoPlayer.currentMediaItemIndex].copy(
                            isPlaying = isPlaying
                        )
                    )
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    mediaItem
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

val dummySongs = listOf(
    Song(title = "How to Save a Life", artist = "The Fray", album = "Mock Album"),
    Song(title = "Thousand miles", artist = "Vanessa Carlton", album = "Mock Album"),
)