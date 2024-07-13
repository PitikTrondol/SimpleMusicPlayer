package anridiaf.playground.simplemusicplayer.ui

import android.util.Log
import androidx.media3.common.Player

private fun logEvent(events: Player.Events){
    val test = (0 until events.size()).map {
        mapEvent[events.get(it)]
    }
    Log.e("AFRI", "====================")
    Log.e("AFRI", test.joinToString("\n"))
}

private val mapEvent = mapOf(
    Pair(Player.EVENT_TIMELINE_CHANGED, "Player.EVENT_TIMELINE_CHANGED"),
    Pair(Player.EVENT_MEDIA_ITEM_TRANSITION, "Player.EVENT_MEDIA_ITEM_TRANSITION"),
    Pair(Player.EVENT_TRACKS_CHANGED, "Player.EVENT_TRACKS_CHANGED"),
    Pair(Player.EVENT_IS_LOADING_CHANGED, "Player.EVENT_IS_LOADING_CHANGED"),
    Pair(Player.EVENT_PLAYBACK_STATE_CHANGED, "Player.EVENT_PLAYBACK_STATE_CHANGED"),
    Pair(Player.EVENT_PLAY_WHEN_READY_CHANGED, "Player.EVENT_PLAY_WHEN_READY_CHANGED"),
    Pair(Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED, "Player.EVENT_PLAYBACK_SUPPRESSION_REASON_CHANGED"),
    Pair(Player.EVENT_IS_PLAYING_CHANGED, "Player.EVENT_IS_PLAYING_CHANGED"),
    Pair(Player.EVENT_REPEAT_MODE_CHANGED, "Player.EVENT_REPEAT_MODE_CHANGED"),
    Pair(Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED, "Player.EVENT_SHUFFLE_MODE_ENABLED_CHANGED"),
    Pair(Player.EVENT_PLAYER_ERROR, "Player.EVENT_PLAYER_ERROR"),
    Pair(Player.EVENT_POSITION_DISCONTINUITY, "Player.EVENT_POSITION_DISCONTINUITY"),
    Pair(Player.EVENT_PLAYBACK_PARAMETERS_CHANGED, "Player.EVENT_PLAYBACK_PARAMETERS_CHANGED"),
    Pair(Player.EVENT_AVAILABLE_COMMANDS_CHANGED, "Player.EVENT_AVAILABLE_COMMANDS_CHANGED"),
    Pair(Player.EVENT_MEDIA_METADATA_CHANGED, "Player.EVENT_MEDIA_METADATA_CHANGED"),
    Pair(Player.EVENT_SEEK_BACK_INCREMENT_CHANGED, "Player.EVENT_SEEK_BACK_INCREMENT_CHANGED"),
    Pair(Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED, "Player.EVENT_SEEK_FORWARD_INCREMENT_CHANGED"),
    Pair(Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED, "Player.EVENT_MAX_SEEK_TO_PREVIOUS_POSITION_CHANGED"),
    Pair(Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED, "Player.EVENT_TRACK_SELECTION_PARAMETERS_CHANGED"),
    Pair(Player.EVENT_AUDIO_ATTRIBUTES_CHANGED, "Player.EVENT_AUDIO_ATTRIBUTES_CHANGED"),
    Pair(Player.EVENT_AUDIO_SESSION_ID, "Player.EVENT_AUDIO_SESSION_ID"),
)