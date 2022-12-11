package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.room.Room
import ca.dawsoncollege.project_pokemon.databinding.IntroSequenceBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: IntroSequenceBinding
    private lateinit var playerTrainer: PlayerTrainer
    private lateinit var userDao: UserDao
    private var starterPokemon = ""
    private val starters: Map<String, String> = mapOf(
        "grassStarter" to "Bulbasaur",
        "fireStarter" to "Charmander", "waterStarter" to "Squirtle"
    )

    companion object {
        private const val LOG_TAG = "INTRO_ACTIVITY_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroSequenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Trainer-Database"
        ).build()

        this.userDao = db.userDao()

        binding.startBtn.setOnClickListener {
            createPlayerTrainer()
        }

        // TODO: add styling when selected e.g. border
        binding.starterRadioGroup.setOnCheckedChangeListener { radioGroup, optionId ->
            run {
                when (optionId) {
                    R.id.grass_starter_img -> {
                        starterPokemon = starters["grassStarter"].toString()
                    }
                    R.id.fire_starter_img -> {
                        starterPokemon = starters["fireStarter"].toString()
                    }
                    R.id.water_starter_img -> {
                        starterPokemon = starters["waterStarter"].toString()
                    }
                }
                Toast.makeText(applicationContext, starterPokemon, Toast.LENGTH_SHORT).show()
            }
        }
    }

    // checks if name is given and if so, creates playerTrainer
    private fun createPlayerTrainer() {
        // if no player name is given
        if (binding.trainerNameInput.text.toString().isBlank()) {
            Toast.makeText(applicationContext, R.string.missing_trainer_name, Toast.LENGTH_SHORT)
                .show()
        } else {
            this.playerTrainer = PlayerTrainer(binding.trainerNameInput.text.toString())
            if (pickStarter()) {
                // add playerTrainer to SharedPreferences
                runBlocking(Dispatchers.IO) {
                    if (userDao.fetchPlayerSave() != null) userDao.delete()
                    userDao.savePlayerTrainer(this@IntroActivity.playerTrainer)
                }
                Toast.makeText(applicationContext, "added player", Toast.LENGTH_SHORT).show()

                startMainMenuActivity()
            }
//            val json = sharedPreference.getString("playerTrainer", "")
//            if (json != ""){
//                playerTrainer = convertJSONToPlayerTrainer(json!!)
//                Toast.makeText(applicationContext, "Hi " + playerTrainer.name, Toast.LENGTH_SHORT).show()
//            }
        }
    }

    // checks if starter pokemon is picked and if so, assigns it to playerTrainer
    private fun pickStarter(): Boolean {
        // if no starter pokemon is selected
        return if (binding.starterRadioGroup.checkedRadioButtonId == -1) {
            Toast.makeText(applicationContext, R.string.missing_starter_pokemon, Toast.LENGTH_SHORT)
                .show()
            false
        } else {
            Toast.makeText(
                applicationContext,
                "$starterPokemon is your starter!",
                Toast.LENGTH_SHORT
            ).show()
            // if no nickname is given
            if (binding.askNickname.text.toString().isBlank()) {
//                Toast.makeText(applicationContext, "no nickname", Toast.LENGTH_SHORT).show()
                this.playerTrainer.setStarter(starterPokemon, null)
            } else {
//                Toast.makeText(applicationContext, binding.askNickname.text.toString(), Toast.LENGTH_SHORT).show()
                this.playerTrainer.setStarter(starterPokemon, binding.askNickname.text.toString())
            }
            true
        }
    }

    private fun startMainMenuActivity() {
        try {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        } catch (exc: ActivityNotFoundException) {
            Log.e(LOG_TAG, "Could not open MainMenuActivity", exc)
        }
    }
}

// extension functions
// converts PlayerTrainer object into a JSON string
fun convertPlayerTrainerToJSON(playerTrainer: PlayerTrainer): String = Gson().toJson(playerTrainer)

// converts JSON string back into a PlayerTrainer object
fun convertJSONToPlayerTrainer(json: String) =
    Gson().fromJson(json, object : TypeToken<PlayerTrainer>() {}.type) as PlayerTrainer