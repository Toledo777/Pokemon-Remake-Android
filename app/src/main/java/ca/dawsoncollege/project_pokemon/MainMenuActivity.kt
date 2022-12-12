package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import ca.dawsoncollege.project_pokemon.databinding.MainMenuBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainMenuActivity : AppCompatActivity() {
    private lateinit var binding: MainMenuBinding
    private lateinit var playerTrainer: PlayerTrainer
    private lateinit var userDao: UserDao

    companion object {
        private const val LOG_TAG = "MAIN_MENU_ACT_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Trainer-Database"
        ).build()

        this.userDao = db.userDao()

        lifecycleScope.launch(Dispatchers.IO) {
            if (this@MainMenuActivity.userDao.fetchPlayerSave() != null) {
                playerTrainer = this@MainMenuActivity.userDao.fetchPlayerSave()!!
            }
            withContext(Dispatchers.Main){
                setButtonListeners()
                val changeTeamFragment = ChangeTeamFragment()
                // fragment to appear by default
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_menu_fragment, changeTeamFragment)
                    commit()
                }
            }
        }

        // TODO: replace commented code when fragments are ready
        // initialize needed fragments
        //val defaultFragment = MainMenuActivity() // should be a fragment here instead
        //val pokecenterFragment = PokecenterFragment()
//        val tBattleFragment = TrainerBattleFragment()
//        val wBattleFragment = BattleActivity()

    }

    override fun onStart() {
        super.onStart()

        lifecycleScope.launch(Dispatchers.IO) {
            if (this@MainMenuActivity.userDao.fetchPlayerSave() != null) {
                playerTrainer = this@MainMenuActivity.userDao.fetchPlayerSave()!!
            }
            withContext(Dispatchers.Main) {
                val changeTeamFragment = ChangeTeamFragment()
                // fragment to appear by default
                supportFragmentManager.beginTransaction().apply {
                    replace(R.id.main_menu_fragment, changeTeamFragment)
                    commit()
                }
            }
        }
    }

    private fun setButtonListeners() {
        binding.pokecenterBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
//                replace(R.id.frameLayout3, pokecenterFragment)
                // allows back button to go to previous fragment
//                addToBackStack(null)
//                commit()
//            }
                Toast.makeText(applicationContext, "pokecenter", Toast.LENGTH_SHORT).show()
            }
        }
        binding.changeTeamBtn.setOnClickListener {
            supportFragmentManager.beginTransaction().apply {
                replace(R.id.main_menu_fragment, ChangeTeamFragment())
                addToBackStack(null)
                commit()
            }
        }
        binding.trainerBattleBtn.setOnClickListener {
            try {
                if(this.playerTrainer.checkTeamFainted()){
                    val intent = Intent(this, BattleActivity::class.java)
                    intent.putExtra("type", "trainer")
                    startActivity(intent)
                    Toast.makeText(applicationContext, "trainer battle", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(applicationContext, "no pokemon available", Toast.LENGTH_SHORT).show()
            } catch (exc: ActivityNotFoundException){
                Log.e(LOG_TAG, "Could not open BattleActivity", exc)
            } catch (e: Exception){
                Log.e(LOG_TAG, e.message.toString())
            }
        }
        binding.wildBattleBtn.setOnClickListener {
            try {
                if (this.playerTrainer.checkTeamFainted()){
                    val intent = Intent(this, BattleActivity::class.java)
                    intent.putExtra("type", "wild")
                    startActivity(intent)
                    Toast.makeText(applicationContext, "wild battle", Toast.LENGTH_SHORT).show()
                } else
                    Toast.makeText(applicationContext, "no pokemon available", Toast.LENGTH_SHORT).show()
            } catch (exc: ActivityNotFoundException){
                Log.e(LOG_TAG, "Could not open BattleActivity", exc)
            } catch (e: Exception){
                Log.e(LOG_TAG, e.message.toString(), e)
            }
        }
        binding.saveBtn.setOnClickListener {
            runBlocking(Dispatchers.IO) {
                if (userDao.fetchPlayerSave() != null) userDao.delete()
                userDao.savePlayerTrainer(this@MainMenuActivity.playerTrainer)
            }
            finish()
            overridePendingTransition(0, 0)
            startActivity(intent)
            overridePendingTransition(0, 0)
        }
    }
}
