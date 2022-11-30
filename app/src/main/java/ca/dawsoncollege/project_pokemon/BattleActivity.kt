package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import ca.dawsoncollege.project_pokemon.databinding.ActivityBattleBinding

class BattleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBattleBinding
    companion object {
        private const val LOG_TAG = "MAIN_ACTIVITY_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}