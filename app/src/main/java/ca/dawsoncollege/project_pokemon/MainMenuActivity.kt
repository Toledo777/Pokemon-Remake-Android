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
        private const val LOG_TAG = "MAIN_MENU_ACT_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: replace commented code when fragments are ready

        // initialize needed fragments
        //val defaultFragment = MainMenuActivity() // should be a fragment here instead
        //val pokecenterFragment = PokecenterFragment()
//        val changeTeamFragment = ChangeTeamFragment()
//        val tBattleFragment = TrainerBattleFragment()
//        val wBattleFragment = BattleActivity()

        // fragment to appear by default
//        supportFragmentManager.beginTransaction().apply {
//            replace(R.id.main_menu_fragment, defaultFragment)
//            commit()
//        }

        setButtonListeners()
    }

    private fun setButtonListeners() {
        binding.pokecenterBtn.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.main_menu_fragment, pokecenterFragment)
                // allows back button to go to previous fragment
//                addToBackStack(null)
//                commit()
//            }
                Toast.makeText(applicationContext, "pokecenter", Toast.LENGTH_SHORT).show()
            }
            binding.changeTeamBtn.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.main_menu_fragment, changeTeamFragment)
//                addToBackStack(null)
//                commit()
//            }
                Toast.makeText(applicationContext, "change team", Toast.LENGTH_SHORT).show()
            }
            binding.trainerBattleBtn.setOnClickListener {
//            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.main_menu_fragment, tBattleFragment)
//                addToBackStack(null)
//                commit()
//            }
            Toast.makeText(applicationContext, "trainer battle", Toast.LENGTH_SHORT).show()
        }
        binding.wildBattleBtn.setOnClickListener {
            /*supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_menu_fragment, wBattleFragment)
                addToBackStack(null)
                commit()
            }*/
            // TODO: send code or data representing wild battle and not trainer
            try {
                val intent = Intent(this, BattleActivity::class.java)
                startActivity(intent)
            } catch (exc: ActivityNotFoundException){
                Log.e(LOG_TAG, "Could not open BattleActivity", exc)
            }
            Toast.makeText(applicationContext, "wild battle", Toast.LENGTH_SHORT).show()
        }
    }
}