package anridiaf.playground.simplemusicplayer.presenter

import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import anridiaf.playground.simplemusicplayer.sources.songdata.SongData
import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SongManagerViewModel(
    private val songDataManager: SongDataManager,
    private val defaultDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _mutableUiStateFlow = MutableStateFlow<UiState>(UiState.Init)
    val uiStateFlow: StateFlow<UiState> get() = _mutableUiStateFlow

    init {
        viewModelScope.launch {
            _mutableUiStateFlow.emit(UiState.Loading)
            songDataManager.getList().fold(
                failure = {
                    _mutableUiStateFlow.emit(UiState.Error(message = it))
                },
                success = { data ->
                    delay(5000)
                    _mutableUiStateFlow.update {
                        UiState.Success(mapMediaItem(data))
                    }
                }
            )
        }
    }

    private suspend fun mapMediaItem(
        from: List<SongData>
    ): List<MediaItem> = withContext(defaultDispatcher) {
        from.map { data ->
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
        }
    }
}

sealed class UiState {

    data object Init : UiState()
    data object Loading : UiState()
    data class Error(val message: String) : UiState()

    data class Success(val data: List<MediaItem>) : UiState()
}