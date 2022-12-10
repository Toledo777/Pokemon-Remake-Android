package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ca.dawsoncollege.project_pokemon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    companion object {
        private const val LOG_TAG = "MAIN_ACTIVITY_DEV_LOG"
    }

        override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.newGameBtn.setOnClickListener {
            try {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            } catch (exc: ActivityNotFoundException){
                Log.e(LOG_TAG, "Could not open IntroActivity", exc)
            }

        }
    }
}