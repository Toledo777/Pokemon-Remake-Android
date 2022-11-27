package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import ca.dawsoncollege.project_pokemon.databinding.MainMenuBinding

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: MainMenuBinding
    companion object {
        private const val LOG_TAG = "MAIN_MENU_ACTIVITY_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setButtonListeners()
    }

    private fun setButtonListeners(){
        binding.pokecenterBtn.setOnClickListener {
            Toast.makeText(applicationContext, "pokecenter", Toast.LENGTH_SHORT).show()
        }
        binding.changeTeamBtn.setOnClickListener {
            Toast.makeText(applicationContext, "change team", Toast.LENGTH_SHORT).show()
        }
        binding.trainerBattleBtn.setOnClickListener {
            Toast.makeText(applicationContext, "pokecenter", Toast.LENGTH_SHORT).show()
        }
        binding.wildBattleBtn.setOnClickListener {
            Toast.makeText(applicationContext, "change team", Toast.LENGTH_SHORT).show()
        }
    }
}