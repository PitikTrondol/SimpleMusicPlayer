package anridiaf.playground.simplemusicplayer.presenter

import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import anridiaf.playground.simplemusicplayer.sources.songdata.SongData
import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongManagerViewModel(
    private val songDataManager: SongDataManager,
    private val defaultDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _mutableMediaItemFlow = MutableStateFlow(listOf<MediaItem>())
    val mediaItemFlow: StateFlow<List<MediaItem>> get() = _mutableMediaItemFlow

    private val _mutableUiStateSharedFlow = MutableSharedFlow<UiState>()
    val uiStateFlow: SharedFlow<UiState> get() = _mutableUiStateSharedFlow

    init {
        viewModelScope.launch {
            _mutableUiStateSharedFlow.tryEmit(UiState.Loading)
            songDataManager.getList().fold(
                failure = {
                    _mutableUiStateSharedFlow.tryEmit(UiState.Error(message = it))
                },
                success = {data->
                    _mutableMediaItemFlow.update {
                        mapMediaItem(data)
                    }
                }
            )
        }
    }

    private suspend fun mapMediaItem(
        from: List<SongData>
    ): List<MediaItem> = withContext(defaultDispatcher){
        from.map { data->
            MediaItem.Builder()
                .setUri(data.sources)
                .setMediaMetadata(
                    MediaMetadata.Builder()
                        .setTitle(data.title)
                        .setAlbumTitle(data.album)
                        .setArtist(data.artist)
                        .setArtworkUri(data.thumbnail.toUri())
                        .build()
                )
                .build()
        }.also {
            Log.e("AFRI", "mapMediaItem: ${it.joinToString("\n")}")
        }
    }
}

sealed class UiState {
    data object Loading: UiState()
    data class Error(val message: String): UiState()
}