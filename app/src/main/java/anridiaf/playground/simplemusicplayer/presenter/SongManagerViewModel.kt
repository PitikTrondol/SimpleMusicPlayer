package anridiaf.playground.simplemusicplayer.presenter

import androidx.lifecycle.ViewModel
import androidx.media3.common.MediaItem
import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SongManagerViewModel(
    private val songDataManager: SongDataManager
): ViewModel() {

    private val _mutableMediaItemFlow = MutableStateFlow(MediaItem.EMPTY)
    val mediaItemFlow: StateFlow<MediaItem> get() = _mutableMediaItemFlow


}