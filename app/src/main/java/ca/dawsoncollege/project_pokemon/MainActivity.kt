package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Intent
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import ca.dawsoncollege.project_pokemon.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userDao: UserDao

    companion object {
        private const val LOG_TAG = "MAIN_ACTIVITY_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Trainer-Database"
        ).build()
        this.userDao = db.userDao()

        lifecycleScope.launch(Dispatchers.IO) {
            if (this@MainActivity.userDao.fetchPlayerSave() != null) {
                binding.continueBtn.visibility = View.VISIBLE
                binding.continueBtn.setOnClickListener { launchMenu() }
            }
        }

        binding.newGameBtn.setOnClickListener {
            try {
                val intent = Intent(this, IntroActivity::class.java)
                startActivity(intent)
            } catch (exc: ActivityNotFoundException) {
                Log.e(LOG_TAG, "Could not open IntroActivity", exc)
            }

        }

    }

    private fun launchMenu() {
        try {
            val intent = Intent(this, MainMenuActivity::class.java)
            startActivity(intent)
        } catch (exc: ActivityNotFoundException) {
            Log.e(LOG_TAG, "Could not open MainMenuActivity", exc)
        }
    }
}