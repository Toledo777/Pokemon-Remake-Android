package ca.dawsoncollege.project_pokemon

import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import ca.dawsoncollege.project_pokemon.databinding.ActivityBattleBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

class BattleActivity : AppCompatActivity(), Callbacks {
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
                    setMovesFragment()
                }
            }
        }
        binding.movesBtn.setOnClickListener {
            setMovesFragment()
        }
        binding.switchBtn.setOnClickListener {
            setSwitchPokemonFragment()
        }
        binding.itemsBtn.setOnClickListener {
            setItemsFragment()
        }
        binding.runBtn.setOnClickListener {
            // TODO: save/send PlayerTrainer data back
            finish()
        }
    }

    // set entire player pokemon UI
    private fun setPlayerPokemonUI(){
        binding.playerPokemonName.text = this.battle.playerPokemon.name.toString()
        lifecycleScope.launch(Dispatchers.IO){
            val backSprite = BitmapFactory.decodeStream(URL(this@BattleActivity.battle.playerPokemon.data.backSprite).openConnection().getInputStream())
            withContext(Dispatchers.Main) {
                binding.playerPokemonSprite.setImageBitmap(backSprite)
            }
        }
        val pokemonLevelText = "LV "+this.battle.playerPokemon.level.toString()
        binding.playerPokemonLevel.text = pokemonLevelText
        updateHP(this.battle.playerPokemon, true)
    }

    // set entire enemy pokemon UI
    private fun setEnemyPokemonUI(){
        binding.enemyPokemonName.text = this.battle.enemyPokemon.name.toString()
        lifecycleScope.launch(Dispatchers.IO){
            val frontSprite = BitmapFactory.decodeStream(URL(this@BattleActivity.battle.enemyPokemon.data.frontSprite).openConnection().getInputStream())
            withContext(Dispatchers.Main) {
                binding.enemyPokemonSprite.setImageBitmap(frontSprite)
            }
        }
        val pokemonLevelText = "LV "+this.battle.enemyPokemon.level.toString()
        binding.enemyPokemonLevel.text = pokemonLevelText
        updateHP(this.battle.enemyPokemon, false)
    }

    // update HP UI of given pokemon
    private fun updateHP(pokemon: Pokemon, isPlayer: Boolean){
        val hp = pokemon.hp.toString() + "/" + pokemon.battleStat.maxHP.toString()
        if (isPlayer){
            binding.playerPokemonHealth.text = hp
        } else {
            binding.enemyPokemonHealth.text = hp
        }
    }

    // start moves fragment
    private fun setMovesFragment(){
        val movesFragment = MovesFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        movesFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, movesFragment)
            commit()
        }
    }

    // start switch pokemon fragment
    private fun setSwitchPokemonFragment(){
        val switchPokemonFragment = SwitchPokemonFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        switchPokemonFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, switchPokemonFragment)
            commit()
        }
    }

    // start items fragment
    private fun setItemsFragment(){
        val itemsFragment = ItemsFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        itemsFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, itemsFragment)
            commit()
        }
    }

    // callback for fragments to update battle data in this activity
    @Override
    override fun updateTeam(battle: Battle) {
        this.battle = battle
    }

    // callback for fragments to update battle data and HP UI in this activity
    @Override
    override fun updateHPUI(battle: Battle) {
        this.battle = battle
        this.playerTrainer = battle.playerTrainer
        updateHP(this.battle.playerPokemon, true)
        updateHP(this.battle.enemyPokemon, false)
    }

    // callback for fragments to update battle data and entire battle UI in this activity
    @Override
    override fun updatePokemonUI(battle: Battle) {
        val oldBattle = this.battle
        this.battle = battle
        if (this.battle.playerPokemon != oldBattle.playerPokemon){
            setPlayerPokemonUI()
        }
        if (this.battle.enemyPokemon != oldBattle.enemyPokemon){
            setEnemyPokemonUI()
        }
    }
}

// interface containing callbacks
interface Callbacks {
    fun updateTeam(battle: Battle)
    fun updateHPUI(battle: Battle)
    fun updatePokemonUI(battle: Battle)
}

// extension functions
// converts Battle object into a JSON string
fun convertBattleToJSON(battle: Battle): String = Gson().toJson(battle)
// converts JSON string back into a Wild Battle object
fun convertJSONToWildBattle(json: String) = Gson().fromJson(json, object: TypeToken<WildBattle>(){}.type) as WildBattle