package anridiaf.playground.simplemusicplayer.presenter

import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import anridiaf.playground.simplemusicplayer.utils.defaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module

val presenterModule = module {
    viewModel<SongManagerViewModel> {
        SongManagerViewModel(
            songDataManager = get<SongDataManager>(),
            defaultDispatcher = get<CoroutineDispatcher>(qualifier = named(defaultDispatcher))
        )
    }
}