package net.moisesborges.audioplayer

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import net.moisesborges.ui.audioplayer.AudioPlayerViewModel

private const val ACTION_PLAY_STOP = "net.moisesborges.audioplayer.ACTION_PLAY_STOP"

class AudioPlayerBroadcastReceiver(
    private val viewModel: AudioPlayerViewModel
) : BroadcastReceiver() {

    fun register(context: Context) {
        val filter = IntentFilter(ACTION_PLAY_STOP)
        context.registerReceiver(this, filter)
    }

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            ACTION_PLAY_STOP -> {
                viewModel.playPause()
            }
            else -> {
                throw IllegalStateException("${AudioPlayerBroadcastReceiver::class.simpleName} cannot handle ${intent.action} action")
            }
        }
    }
}

fun createPlayStopIntent() = Intent(ACTION_PLAY_STOP)