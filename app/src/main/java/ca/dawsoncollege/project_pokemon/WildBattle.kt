package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.random.Random

class WildBattle(playerTrainer: PlayerTrainer, context: Context): Battle(playerTrainer, context) {

    init {
        // generates wild pokemon and sets it as enemy pokemon
        generateWildPokemon()
    }
    // generates random wild pokemon depending on trainer level
    private fun generateWildPokemon() {
        // get random level for wild pokemon
        val wildLevel = playerTrainer.getRandomEnemyLevel()

        // TODO un hardcode pokemons
        enemyPokemon =  Pokemon(context, wildLevel, "caterpie")
       // enemyPokemon = Pokemon()
    }

    // throw pokeball to attempt catch
    fun throwPokeball(): Boolean {
        val success = playerTrainer.catchPokemon(playerPokemon, enemyPokemon)
        // boolean indicating catch success or fail
        return success;
    }
}