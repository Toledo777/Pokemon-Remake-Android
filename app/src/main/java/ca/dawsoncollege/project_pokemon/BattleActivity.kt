package ca.dawsoncollege.project_pokemon

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import ca.dawsoncollege.project_pokemon.databinding.ActivityBattleBinding
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.net.URL


class BattleActivity : AppCompatActivity(), Callbacks {
    private lateinit var binding: ActivityBattleBinding
    private lateinit var battle: Battle
    private lateinit var battleType: String
    private lateinit var playerTrainer: PlayerTrainer
    private lateinit var enemyTrainer: EnemyTrainer
    private lateinit var userDao: UserDao

    companion object {
        private const val LOG_TAG = "BATTLE_ACTIVITY_DEV_LOG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBattleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val bundle: Bundle? = intent.extras
        battleType = bundle!!.getString("type").toString()

//        val sharedPreference = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE)
//        val playerTrainerJson = sharedPreference.getString("playerTrainer", "empty")
//        if (playerTrainerJson != "empty") {
//            playerTrainer = convertJSONToPlayerTrainer(playerTrainerJson!!)
//            lifecycleScope.launch(Dispatchers.IO){
//
        val db = Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "Trainer-Database"
        ).build()

        this.userDao = db.userDao()

        lifecycleScope.launch(Dispatchers.IO) {
            if (this@BattleActivity.userDao.fetchPlayerSave() != null) {
                playerTrainer = this@BattleActivity.userDao.fetchPlayerSave()!!
                if (battleType == "wild")
                    this@BattleActivity.battle = WildBattle(playerTrainer)
                else{
                    this@BattleActivity.enemyTrainer = EnemyTrainer()
                    this@BattleActivity.battle = TrainerBattle(playerTrainer, this@BattleActivity.enemyTrainer)
                }
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
            if (battleType == "wild"){
                this.battle.updatePlayerPokemon()
                this.battle = this.battle.playerRun()
                this.playerTrainer = this.battle.playerTrainer
                runBlocking(Dispatchers.IO) {
                    if (userDao.fetchPlayerSave() != null) userDao.delete()
                    userDao.savePlayerTrainer(this@BattleActivity.playerTrainer)
                }
                finish()
            } else
                updateBattleText("You can't run from a trainer battle.")
        }
    }

    // set entire player pokemon UI
    private fun setPlayerPokemonUI() {
        binding.playerPokemonName.text = this.battle.playerPokemon.name.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val backSprite = BitmapFactory.decodeStream(
                URL(this@BattleActivity.battle.playerPokemon.data.backSprite).openConnection()
                    .getInputStream()
            )
            withContext(Dispatchers.Main) {
                binding.playerPokemonSprite.setImageBitmap(backSprite)
            }
        }
        val pokemonLevelText = "LV " + this.battle.playerPokemon.level.toString()
        binding.playerPokemonLevel.text = pokemonLevelText
        updateHP(this.battle.playerPokemon, true)
    }

    // set entire enemy pokemon UI
    private fun setEnemyPokemonUI() {
        binding.enemyPokemonName.text = this.battle.enemyPokemon.name.toString()
        lifecycleScope.launch(Dispatchers.IO) {
            val frontSprite = BitmapFactory.decodeStream(
                URL(this@BattleActivity.battle.enemyPokemon.data.frontSprite).openConnection()
                    .getInputStream()
            )
            withContext(Dispatchers.Main) {
                binding.enemyPokemonSprite.setImageBitmap(frontSprite)
            }
        }
        val pokemonLevelText = "LV " + this.battle.enemyPokemon.level.toString()
        binding.enemyPokemonLevel.text = pokemonLevelText
        updateHP(this.battle.enemyPokemon, false)
    }

    // update HP UI of given pokemon
    private fun updateHP(pokemon: Pokemon, isPlayer: Boolean) {
        val hp = pokemon.hp.toString() + "/" + pokemon.battleStat.maxHP.toString()
        if (isPlayer) {
            binding.playerPokemonHealth.text = hp
        } else {
            binding.enemyPokemonHealth.text = hp
        }
    }

    // start moves fragment
    private fun setMovesFragment() {
        val movesFragment = MovesFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        bundle.putString("type", battleType)
        movesFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, movesFragment)
            commit()
        }
    }

    // start switch pokemon fragment
    private fun setSwitchPokemonFragment() {
        val switchPokemonFragment = SwitchPokemonFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        bundle.putString("type", battleType)
        switchPokemonFragment.arguments = bundle
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, switchPokemonFragment)
            commit()
        }
    }

    // start items fragment
    private fun setItemsFragment() {
        val itemsFragment = ItemsFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        bundle.putString("type", battleType)
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
        this.battle = battle
        setPlayerPokemonUI()
        setEnemyPokemonUI()
    }

    // callback for fragments to update battle text view
    @Override
    override fun updateBattleText(message: String) {
        binding.battleText.text = message;
    }

    @Override
    override fun onBackPressed() {
        // super.onBackPressed();
    }
}

// interface containing callbacks
interface Callbacks {
    fun updateTeam(battle: Battle)
    fun updateHPUI(battle: Battle)
    fun updatePokemonUI(battle: Battle)
    fun updateBattleText(message: String)
}

// extension functions
// converts Battle object into a JSON string
fun convertBattleToJSON(battle: Battle): String = Gson().toJson(battle)
// converts JSON string back into a Wild Battle object
fun convertJSONToWildBattle(json: String) =
    Gson().fromJson(json, object: TypeToken<WildBattle>(){}.type) as WildBattle
// converts JSON string back into a Trainer Battle object
fun convertJSONToTrainerBattle(json: String) =
    Gson().fromJson(json, object: TypeToken<TrainerBattle>(){}.type) as TrainerBattle
