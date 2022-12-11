package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.random.Random

class WildBattle(playerTrainer: PlayerTrainer): Battle(playerTrainer) {

    init {
        // generates wild pokemon and sets it as enemy pokemon
        generateWildPokemon()
    }
    // generates random wild pokemon depending on trainer level
    private fun generateWildPokemon() {
        // get random level for wild pokemon
        val wildLevel = playerTrainer.getRandomEnemyLevel()

        enemyPokemon =  Pokemon(wildLevel)
       // enemyPokemon = Pokemon()
    }

    // check if pokemon fainted, awards xp if so
    // should be called after every move
    override fun checkPokemonFainted(): Boolean {
        if (enemyPokemon.hp == 0) {
            // give player's pokemon exp
            gainExperience()
               return true
        }
        return false
    }

    override fun gainExperience() {
        val expGained = (0.3 * this.enemyPokemon.data.baseExperienceReward * this.enemyPokemon.level).toInt()
        // adds exp and levels up if possible
        this.playerPokemon.addExp(expGained)
    }

    // throw pokeball to attempt catch
    fun throwPokeball(): Boolean {
        val success = playerTrainer.catchPokemon(playerPokemon, enemyPokemon)
        // boolean indicating catch success or fail
        return success;
    }
}