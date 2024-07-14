package anridiaf.playground.simplemusicplayer.presenter

import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import anridiaf.playground.simplemusicplayer.utils.CoroutinesTestExtension
import anridiaf.playground.simplemusicplayer.utils.InstantExecutorExtension
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension

@OptIn(ExperimentalCoroutinesApi::class)
class SongManagerViewModelTest {

    private lateinit var sut : SongManagerViewModel
    private val songDataManager: SongDataManager = mockk()

    @JvmField
    @RegisterExtension
    val coroutinesExtension = CoroutinesTestExtension()

    @JvmField
    @RegisterExtension
    val executorExtension = InstantExecutorExtension()

    @BeforeEach
    fun setup(){
        sut = SongManagerViewModel(
            songDataManager = songDataManager,
            defaultDispatcher = coroutinesExtension.dispatcher
        )
    }

    @Test
    fun `dasd `() = runTest {  }

}