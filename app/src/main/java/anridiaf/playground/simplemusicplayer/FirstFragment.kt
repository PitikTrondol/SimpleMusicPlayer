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
        binding.controller.visibility = View.INVISIBLE
        binding.controller.player = exoPlayer
        binding.playlist.layoutManager = LinearLayoutManager(requireContext())
        binding.playlist.adapter = SongItemAdapter(dummySongs){
            exoPlayer.setMediaItem(MediaItem.fromUri(uri))
            exoPlayer.prepare()
            binding.controller.visibility = View.VISIBLE
        }
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