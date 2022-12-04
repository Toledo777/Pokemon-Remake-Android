package ca.dawsoncollege.project_pokemon

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.lifecycleScope
import ca.dawsoncollege.project_pokemon.databinding.ActivityBattleBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL

class BattleActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBattleBinding
    private lateinit var battle: Battle
    private lateinit var playerTrainer: PlayerTrainer
    companion object {
        private const val LOG_TAG = "BATTLE_ACTIVITY_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // TODO: if trainer battle, init enemy trainer (doesn't need to be top level)
        val sharedPreference = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
        val playerTrainerJson = sharedPreference.getString("playerTrainer", "empty")
        if (playerTrainerJson != "empty") {
            playerTrainer = convertJSONToPlayerTrainer(playerTrainerJson!!)
            lifecycleScope.launch(Dispatchers.IO){
                this@BattleActivity.battle = WildBattle(playerTrainer)
                withContext(Dispatchers.Main){
                    setPlayerPokemonUI()
                    setEnemyPokemonUI()
                    setDefaultFragment()
                }
            }
        }
    }

    private fun setPlayerPokemonUI(){
        // TODO: set imageview to sprite of pokemon using bitmap
        if (this.battle.playerPokemon.name.isNullOrEmpty()){
            binding.playerPokemonName.text = this.battle.playerPokemon.species.toString()
        } else {
            binding.playerPokemonName.text = this.battle.playerPokemon.name.toString()
        }
        lifecycleScope.launch(Dispatchers.IO){
            val backSprite = BitmapFactory.decodeStream(URL(this@BattleActivity.battle.playerPokemon.data.backSprite).openConnection().getInputStream())
            withContext(Dispatchers.Main) {
                binding.playerPokemonSprite.setImageBitmap(backSprite)
            }
        }
        binding.playerPokemonLevel.text = this.battle.playerPokemon.level.toString()
        updateHP(this.battle.playerPokemon, true)
    }

    private fun setEnemyPokemonUI(){
        binding.enemyPokemonName.text = this.battle.enemyPokemon.name.toString()
        lifecycleScope.launch(Dispatchers.IO){
            val frontSprite = BitmapFactory.decodeStream(URL(this@BattleActivity.battle.enemyPokemon.data.frontSprite).openConnection().getInputStream())
            withContext(Dispatchers.Main) {
                binding.enemyPokemonSprite.setImageBitmap(frontSprite)
            }
        }
        binding.enemyPokemonLevel.text = this.battle.enemyPokemon.level.toString()
        updateHP(this.battle.enemyPokemon, false)
    }

    private fun updateHP(pokemon: Pokemon, isPlayer: Boolean){
        val hp = pokemon.hp.toString() + "/" + pokemon.battleStat.maxHP.toString()
        if (isPlayer){
            binding.playerPokemonHealth.text = hp
        } else {
            binding.enemyPokemonHealth.text = hp
        }
    }

    private fun setDefaultFragment(){
        val movesFragment = MovesFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        movesFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, movesFragment)
            commit()
        }
    }
}

// extension functions
// converts Battle object into a JSON string
fun convertBattleToJSON(battle: Battle): String = Gson().toJson(battle)
// converts JSON string back into a Battle object
fun convertJSONToWildBattle(json: String) = Gson().fromJson(json, object: TypeToken<WildBattle>(){}.type) as WildBattle