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
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import anridiaf.playground.simplemusicplayer.databinding.FragmentFirstBinding

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@OptIn(UnstableApi::class)
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val exoPlayer: ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }

    private val onlineTracks = songUrl.map {
        MediaItem.Builder()
            .setUri(it)
            .setMediaMetadata(
                MediaMetadata.Builder()
                    .build()
            )
            .build()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = SongItemAdapter(onlineTracks) { index ->

        }
        binding.playlist.layoutManager = LinearLayoutManager(requireContext())
        binding.playlist.adapter = adapter

        binding.controller.player = exoPlayer
        exoPlayer.addMediaItems(onlineTracks)
        exoPlayer.prepare()
        exoPlayer.addListener(
            object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    super.onMediaItemTransition(mediaItem, reason)
                    Log.e("AFRI", "onMediaItemTransition: ${mediaItem?.mediaMetadata?.artworkUri}")
                    Log.e("AFRI", "onMediaItemTransition: ${mediaItem?.mediaMetadata?.artworkDataType}")
                }

                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                    Log.e("AFRI", "onMediaMetadataChanged: ${mediaMetadata.artist}")
                    super.onMediaMetadataChanged(mediaMetadata)
                }
            }
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

val songUrl = listOf(
    "https://cdn.pixabay.com/download/audio/2024/01/25/audio_599143a433.mp3?filename=beyond-horizons-187963.mp3",
    "https://cdn.pixabay.com/download/audio/2023/12/14/audio_45a981149b.mp3?filename=fvcksin-181360.mp3",
    "https://cdn.pixabay.com/download/audio/2024/05/10/audio_ba4c81d07e.mp3?filename=one-step-closer-207958.mp3",
    "https://cdn.pixabay.com/download/audio/2024/05/08/audio_373e05c162.mp3?filename=soaring-heights-207507.mp3",
    "https://cdn.pixabay.com/download/audio/2024/04/14/audio_52070f6f9b.mp3?filename=happy-202230.mp3",
    "https://cdn.pixabay.com/download/audio/2021/12/01/audio_e2a795a65c.mp3?filename=words-unsaid-11540.mp3",
    "https://cdn.pixabay.com/download/audio/2024/06/26/audio_69c4bb112f.mp3?filename=epic-piano-music-deception-point-219963.mp3",
    "https://cdn.pixabay.com/download/audio/2023/09/17/audio_6b45d4469c.mp3?filename=ambition-multiple-asian-music-instruments-166931.mp3",
    "https://cdn.pixabay.com/download/audio/2021/08/27/audio_759906caf1.mp3?filename=uplifting-piano-background-music-for-videos-7770.mp3",
)