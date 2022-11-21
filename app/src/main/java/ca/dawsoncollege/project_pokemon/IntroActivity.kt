package ca.dawsoncollege.project_pokemon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ca.dawsoncollege.project_pokemon.databinding.IntroSequenceBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: IntroSequenceBinding
    private lateinit var playerTrainer: PlayerTrainer
    private val starters: Map<String, String> = mapOf("grassStarter" to "Bulbasaur",
        "fireStarter" to "Charmander", "waterStarter" to "Squirtle")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroSequenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            verifyInputs()
        }
    }

    // verifies and validates inputs to create player
    // TODO: divide into smaller methods if possible
    private fun verifyInputs(){
        var starterPokemon = ""
        if (binding.trainerNameInput.text.toString().isBlank()){
            Toast.makeText(applicationContext, R.string.missing_trainer_name, Toast.LENGTH_SHORT).show()
        } else {
            playerTrainer = PlayerTrainer(binding.trainerNameInput.text.toString())
            if (binding.starterRadioGroup.checkedRadioButtonId == -1){
                Toast.makeText(applicationContext, R.string.missing_starter_pokemon, Toast.LENGTH_SHORT).show()
            } else {
                when (binding.starterRadioGroup.checkedRadioButtonId) {
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
                Toast.makeText(applicationContext, "$starterPokemon is your starter!", Toast.LENGTH_SHORT).show()

                if(binding.askNickname.text.toString().isBlank()){
                    Toast.makeText(applicationContext, "no nickname", Toast.LENGTH_SHORT).show()
//                    playerTrainer.createStarter(starterPokemon, null)
                } else {
                    Toast.makeText(applicationContext, binding.askNickname.text.toString(), Toast.LENGTH_SHORT).show()
//                    playerTrainer.createStarter(starterPokemon, binding.askNickname.text.toString())
                }
                val sharedPreference = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
                val editor = sharedPreference.edit()
                editor.putString("playerTrainer", convertToJSON(playerTrainer))
                editor.apply()
//                val json = sharedPreference.getString("playerTrainer", "")
//                if (json != ""){
//                    playerTrainer = convertToPlayerTrainer(json!!)
//                    Toast.makeText(applicationContext, "Hi " + playerTrainer.name, Toast.LENGTH_SHORT).show()
//                }
            }
        }
    }
}

// extension functions
// converts PlayerTrainer object into a JSON string
fun convertToJSON(playerTrainer: PlayerTrainer): String = Gson().toJson(playerTrainer)
// converts JSON string back into a PlayerTrainer object
fun convertToPlayerTrainer(json: String) = Gson().fromJson(json, object: TypeToken<PlayerTrainer>(){}.type) as PlayerTrainer