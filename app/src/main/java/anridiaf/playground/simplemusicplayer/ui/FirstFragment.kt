package anridiaf.playground.simplemusicplayer.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.OptIn
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.util.EventLogger
import androidx.recyclerview.widget.LinearLayoutManager
import anridiaf.playground.simplemusicplayer.R
import anridiaf.playground.simplemusicplayer.databinding.FragmentFirstBinding
import anridiaf.playground.simplemusicplayer.presenter.SongManagerViewModel
import anridiaf.playground.simplemusicplayer.presenter.UiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import kotlin.properties.Delegates

@OptIn(UnstableApi::class)
class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding get() = _binding!!
    private val exoPlayer: ExoPlayer by lazy {
        ExoPlayer.Builder(requireContext())
            .build().apply {
                addAnalyticsListener(EventLogger())
            }
    }
    private val viewModel: SongManagerViewModel by viewModel()
    private val adapter: SongItemAdapter by lazy {
        SongItemAdapter { seekToIndex = it }
    }

    /** Debouncing query search to deffer searching process after finished typing */
    private val _mutableSearchFlow: MutableSharedFlow<String> = MutableSharedFlow(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    @kotlin.OptIn(FlowPreview::class)
    private val _searchFlow: Flow<String>
        get() = _mutableSearchFlow
            .debounce(700)
            .distinctUntilChanged()

    /** For easier diff to minimize method call of [SongItemAdapter.updateItem] */
    private var currentPlaying: Pair<MediaItem, Boolean> by Delegates.observable(
        Pair(MediaItem.EMPTY, false)
    ) { _, old, new ->
        if (old != new) {
            adapter.updateItem(new)
        }
    }

    private var seekToIndex: Int by Delegates.observable(-1) { _, old, new ->
        if (old != new) {
            adapter.selectItem(new)
            if (!exoPlayer.isPlaying) {
                exoPlayer.seekTo(new, 0L)
            }
        }

        if (new > -1) {
            binding.controller.visibility = View.VISIBLE
            val color = resources.getColor(R.color.transparent, resources.newTheme())
            binding.controllerContainer.setBackgroundColor(color)
        }
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

        // SearchView
        binding.searchView.setOnQueryTextListener(queryListener)
        binding.searchView.queryHint = getString(R.string.search_hint)

        // RecyclerView
        binding.playlist.layoutManager = LinearLayoutManager(requireContext())
        binding.playlist.adapter = adapter

        // ExoPlayer
        exoPlayer.addListener(playerListener)
        binding.controller.player = exoPlayer
        binding.controller.visibility = View.INVISIBLE

        observeUIState()
        observeSearchFlow()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exoPlayer.release()
        _binding = null
    }

    private fun observeUIState() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.uiStateFlow.collect { state ->
                    when (state) {
                        UiState.Init -> {
                            // Do Nothing
                        }

                        UiState.Loading -> {
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

    private fun observeSearchFlow() {
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                _searchFlow.collect {
                    viewModel.filterPlaylist(it)
                }
            }
        }
    }

    private val playerListener = object : Player.Listener {
        override fun onEvents(player: Player, events: Player.Events) {
            super.onEvents(player, events)
            player.currentMediaItem?.let {
                currentPlaying = Pair(
                    first = it,
                    second = player.isPlaying
                )
            }
        }

        override fun onPositionDiscontinuity(
            oldPosition: Player.PositionInfo,
            newPosition: Player.PositionInfo,
            reason: Int
        ) {
            super.onPositionDiscontinuity(oldPosition, newPosition, reason)

            val oldIndex = oldPosition.mediaItemIndex
            val newIndex = newPosition.mediaItemIndex
            if (oldIndex == newIndex && reason == Player.DISCONTINUITY_REASON_SEEK_ADJUSTMENT) {
                if(!exoPlayer.isPlaying){
                    exoPlayer.play()
                }
            }
        }
    }

    private val queryListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            if (newText.isNullOrBlank()) {
                // For immediate effect without debounce
                viewModel.filterPlaylist("")
            } else {
                _mutableSearchFlow.tryEmit(newText)
            }

            return false
        }

    }
}