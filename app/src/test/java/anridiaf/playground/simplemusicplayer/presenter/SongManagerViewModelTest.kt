package anridiaf.playground.simplemusicplayer.presenter

import android.net.Uri
import androidx.media3.common.MediaItem
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.withContext
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.api.extension.RegisterExtension
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
@ExtendWith(InstantExecutorExtension::class)
class SongManagerViewModelTest {

    @JvmField
    @RegisterExtension
    val coroutinesExtension = CoroutinesTestExtension()

    private val songDataManager: SongDataManager = mockk()
    private fun createViewModel() = SongManagerViewModel(
        songDataManager = songDataManager,
        defaultDispatcher = coroutinesExtension.dispatcher
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
                val sut = createViewModel()

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
                val sut = createViewModel()

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
                val sut = createViewModel()

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
        fun `WHEN query matched 1 THEN uiStateFlow data SHOULD exist 1 member`() = runTest {
            // Arrange
            val expectedName = "Haf"
            val expectedData = listMediaItem
            every { Uri.parse(any()) } returns mockk<Uri>()
            coEvery { songDataManager.getList() } returns ResultWrapper.Success(expectedData)

            // Act
            val sut = createViewModel()
            sut.uiStateFlow.test{
                assertTrue(awaitItem() is UiState.Init)
                assertTrue(awaitItem() is UiState.Loading)

                val afterInit = awaitItem()
                assertTrue(afterInit is UiState.Success)
                assertEquals(expectedData.size, afterInit.data.size)
            }
            advanceUntilIdle()

            sut.filterPlaylist(expectedName)
            advanceUntilIdle()

            // Assert
            sut.uiStateFlow.test {
                val afterFiltering = awaitItem()
                assertTrue(afterFiltering is UiState.Success)
                assertEquals(1, afterFiltering.data.size)
            }
        }

        @Test
        fun `WHEN query not matched THEN uiStateFlow data SHOULD BE empty`() = runTest {
            // Arrange
            val expectedName = "Hafdasdasdas"
            val expectedData = listMediaItem
            every { Uri.parse(any()) } returns mockk<Uri>()
            coEvery { songDataManager.getList() } returns ResultWrapper.Success(expectedData)

            // Act
            val sut = createViewModel()
            sut.uiStateFlow.test {
                assertTrue(awaitItem() is UiState.Init)
                assertTrue(awaitItem() is UiState.Loading)

                val afterInit = awaitItem()
                assertTrue(afterInit is UiState.Success)
                assertEquals(expectedData.size, afterInit.data.size)
            }
            advanceUntilIdle()

            sut.filterPlaylist(expectedName)
            advanceUntilIdle()

            // Assert
            sut.uiStateFlow.test {
                val afterFiltering = awaitItem()
                assertTrue(afterFiltering is UiState.Success)
                assertTrue(afterFiltering.data.isEmpty())
            }
        }

        @Test
        fun `WHEN filtered list, re-query with blank string, THEN uiStateFlow data SHOULD BE reset`() = runTest {
            // Arrange
            val firstQuery = "arno"
            val expectedData = listMediaItem
            every { Uri.parse(any()) } returns mockk<Uri>()
            coEvery { songDataManager.getList() } returns ResultWrapper.Success(expectedData)

            // Act
            val sut = createViewModel()
            sut.uiStateFlow.test {
                assertTrue(awaitItem() is UiState.Init)
                assertTrue(awaitItem() is UiState.Loading)

                val afterInit = awaitItem()
                assertTrue(afterInit is UiState.Success)
                assertEquals(expectedData.size, afterInit.data.size)
            }
            advanceUntilIdle()

            sut.filterPlaylist(firstQuery)
            advanceUntilIdle()

            sut.uiStateFlow.test {
                val afterFiltering = awaitItem()
                assertTrue(afterFiltering is UiState.Success)
                assertEquals(2, afterFiltering.data.size)
            }

            sut.filterPlaylist("")
            advanceUntilIdle()

            // Assert
            sut.uiStateFlow.test {
                val afterFiltering = awaitItem()
                assertTrue(afterFiltering is UiState.Success)
                assertEquals(expectedData.size, afterFiltering.data.size)
            }
        }
    }

    private val listMediaItem = listOf(
        defaultSongData(artis = "Hafnarfjordur"),
        defaultSongData(artis = "Tarno Prok Prok Prok"),
        defaultSongData(title = "Break it down"),
        defaultSongData(artis = "Megumi"),
        defaultSongData(title = "Karno"),
    )

    private fun defaultSongData(artis: String = "", title: String = "") = SongData(
        thumbnail = "",
        sources = "",
        artist = artis,
        title = title,
        album = ""
    )
}