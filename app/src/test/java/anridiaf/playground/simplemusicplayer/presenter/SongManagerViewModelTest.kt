package anridiaf.playground.simplemusicplayer.presenter

import android.net.Uri
import anridiaf.playground.simplemusicplayer.sources.songdata.SongData
import anridiaf.playground.simplemusicplayer.sources.songdata.SongDataManager
import anridiaf.playground.simplemusicplayer.utils.CoroutinesTestExtension
import anridiaf.playground.simplemusicplayer.utils.InstantExecutorExtension
import anridiaf.playground.simplemusicplayer.utils.ResultWrapper
import app.cash.turbine.test
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class SongManagerViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutinesExtension = CoroutinesTestExtension()

    @JvmField
    @RegisterExtension
    val executorExtension = InstantExecutorExtension()

    private val songDataManager: SongDataManager = mockk()
    private fun createViewModel() = SongManagerViewModel(
        songDataManager = songDataManager,
        defaultDispatcher = coroutinesExtension.dispatcher,
    )

    @BeforeEach
    fun setup() {
        mockkStatic(Uri::class)
    }

    @Nested
    inner class OnInit {
        @Test
        fun `WHEN songDataManager return error THEN uiStateFlow SHOULD emit UiState_Error`() =
            runTest {
                // Arrange
                val expectedErrorMessage = "Some error happen"
                coEvery { songDataManager.getList() } returns ResultWrapper.Error(
                    expectedErrorMessage
                )

                // Act
                val sut = SongManagerViewModel(
                    songDataManager = songDataManager,
                    defaultDispatcher = coroutinesExtension.dispatcher,
                )

                // Assert
                sut.uiStateFlow.test {
                    assertTrue(awaitItem() is UiState.Init)
                    assertTrue(awaitItem() is UiState.Loading)
                    val actual = awaitItem()
                    assertTrue(actual is UiState.Error)
                    assertEquals(expectedErrorMessage, actual.message)
                }
            }

        @Test
        fun `WHEN songDataManager return empty THEN uiStateFlow UiState_Success_data SHOULD BE empty`() =
            runTest {
                // Arrange
                coEvery { songDataManager.getList() } returns ResultWrapper.Success(emptyList())

                // Act
                val sut = SongManagerViewModel(
                    songDataManager = songDataManager,
                    defaultDispatcher = coroutinesExtension.dispatcher,
                )

                // Assert
                sut.uiStateFlow.test {
                    assertTrue(awaitItem() is UiState.Init)
                    assertTrue(awaitItem() is UiState.Loading)
                    val actual = awaitItem()
                    assertTrue(actual is UiState.Success)
                    assertEquals(emptyList(), actual.data)
                }
            }

        @Test
        fun `WHEN songDataManager return list THEN uiStateFlow UiState_Success_data SHOULD NOT BE empty`() =
            runTest {
                // Arrange
                val expectedSource = "https://cdn.pixabay.com/"
                val expectedData = listOf(
                    SongData(
                        thumbnail = "thumbnail",
                        sources = expectedSource,
                        artist = "artist",
                        title = "title",
                        album = "album"
                    )
                )

                every { Uri.parse(any()) } returns mockk<Uri>()
                coEvery { songDataManager.getList() } returns ResultWrapper.Success(expectedData)

                // Act
                val sut = SongManagerViewModel(
                    songDataManager = songDataManager,
                    defaultDispatcher = coroutinesExtension.dispatcher,
                )

                // Assert
                sut.uiStateFlow.test {
                    assertTrue(awaitItem() is UiState.Init)
                    assertTrue(awaitItem() is UiState.Loading)

                    val actual = awaitItem()
                    assertTrue(actual is UiState.Success)
                    assertTrue(actual.data.isNotEmpty())
                    val expectedUri = Uri.parse(expectedData[0].sources)
                    val actualUri = actual.data[0].mediaMetadata.artworkUri
                    assertEquals(expectedUri, actualUri)
                }
            }
    }

    @Nested
    inner class Filter {
        @Test
        fun `WHEN query is blank THEN uiStateFlow data SHOULD reset`() = runTest {
            // Arrange

            // Act
            val sut = createViewModel()

            // Assert
        }
    }

    private val listMediaItem = listOf(
        defaultSongData(artis = "Hafnarfjordur"),
        defaultSongData(artis = "Tarno"),
        defaultSongData(title = "Break it down"),
        defaultSongData(artis = "Megumi"),
        defaultSongData(title = "Random"),
    )

    private fun defaultSongData(artis: String = "", title: String = "") = SongData(
        thumbnail = "",
        sources = "",
        artist = title,
        title = title,
        album = ""
    )
}