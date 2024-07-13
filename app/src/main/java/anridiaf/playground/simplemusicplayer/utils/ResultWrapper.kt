package anridiaf.playground.simplemusicplayer.utils

sealed class ResultWrapper<out T : Any> {

    data class Success<T : Any>(
        val data: T
    ) : ResultWrapper<T>()

    data class Error(
        val statusResponse: String = ""
    ) : ResultWrapper<Nothing>()

    inline fun <V> fold(failure: (String) -> V, success: (T) -> V): V = when (this) {
        is Error -> failure(statusResponse)
        is Success -> success(data)
    }
}