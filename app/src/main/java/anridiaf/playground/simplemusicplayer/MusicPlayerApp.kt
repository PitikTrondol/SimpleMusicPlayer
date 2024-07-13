package anridiaf.playground.simplemusicplayer

import android.app.Application
import anridiaf.playground.simplemusicplayer.sources.dataSourceModule
import anridiaf.playground.simplemusicplayer.utils.utilModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MusicPlayerApp: Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(androidContext = applicationContext)
            androidLogger(Level.INFO)
            modules(
                listOf(
                    utilModule,
                    dataSourceModule
                )
            )
        }
    }
}