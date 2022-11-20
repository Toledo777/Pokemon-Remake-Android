package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ca.dawsoncollege.project_pokemon.databinding.IntroSequenceBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class IntroActivity : AppCompatActivity() {
    private lateinit var binding: IntroSequenceBinding
    private lateinit var playerTrainer: PlayerTrainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = IntroSequenceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.startBtn.setOnClickListener {
            verifyInputs()
        }
    }

    // verifies and validates inputs to create player
    // TODO: add data validation (no whitespace/special characters in name)
    private fun verifyInputs(){
        if (binding.trainerNameInput.text.toString().isBlank()){
            Toast.makeText(applicationContext, R.string.missing_trainer_name, Toast.LENGTH_SHORT).show()
        } else {
            // TODO: validate starter pokemon nickname
            Toast.makeText(applicationContext, binding.trainerNameInput.text, Toast.LENGTH_SHORT).show()
            playerTrainer = PlayerTrainer(binding.trainerNameInput.text.toString())
            // TODO: save playerTrainer in shared prefs
        }
    }
}

// extension functions
// converts PlayerTrainer object into a JSON string
fun convertToJSON(playerTrainer: PlayerTrainer): String = Gson().toJson(playerTrainer)
// converts JSON string back into a PlayerTrainer object
fun convertToPlayerTrainer(json: String) = Gson().fromJson(json, object: TypeToken<PlayerTrainer>(){}.type) as PlayerTrainer