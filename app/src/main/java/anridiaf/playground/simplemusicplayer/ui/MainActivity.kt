package anridiaf.playground.simplemusicplayer.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import anridiaf.playground.simplemusicplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}