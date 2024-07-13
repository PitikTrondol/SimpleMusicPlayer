package anridiaf.playground.simplemusicplayer.presenter

import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val presenterModule = module {
    viewModel<SongManagerViewModel> {
        SongManagerViewModel(
            songDataManager = get<SongDataManager>()
        )
    }
}