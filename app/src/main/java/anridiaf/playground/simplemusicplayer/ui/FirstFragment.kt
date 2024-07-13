package anridiaf.playground.simplemusicplayer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.recyclerview.widget.LinearLayoutManager
import anridiaf.playground.simplemusicplayer.databinding.FragmentFirstBinding
import anridiaf.playground.simplemusicplayer.presenter.SongManagerViewModel
import anridiaf.playground.simplemusicplayer.presenter.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
@OptIn(UnstableApi::class)
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val exoPlayer: ExoPlayer by lazy { ExoPlayer.Builder(requireContext()).build() }
    private val viewModel: SongManagerViewModel by viewModel()
    private val adapter: SongItemAdapter by lazy { SongItemAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentFirstBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.controller.player = exoPlayer
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
        binding.playlist.layoutManager = LinearLayoutManager(requireContext())
        binding.playlist.adapter = adapter

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collect{state->
                    when(state){
                        UiState.Init -> {
                            // Do Nothing
                        }

                        UiState.Loading-> {
                            Log.e("AFRI", "onViewCreated: LOADING NDES")
                            binding.loadingScreen.visibility = View.VISIBLE
                        }

                        is UiState.Error -> {
                            binding.loadingScreen.visibility = View.GONE
                            Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT)
                                .show()
                        }

                        is UiState.Success -> {
                            binding.loadingScreen.visibility = View.GONE
                            adapter.updateTracks(state.data)
                            exoPlayer.addMediaItems(state.data)
                            exoPlayer.prepare()
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}