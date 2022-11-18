package ca.dawsoncollege.project_pokemon

import kotlin.random.Random

class WildBattle(playerTrainer: PlayerTrainer): Battle(playerTrainer) {

    // generates random wild pokemon depending on trainer level
    fun generateWildPokemon() {
        val minLevel = playerTrainer.calculateMinTeamLevel()
        val maxLevel = playerTrainer.calculateMaxTeamLevel()
        // get random level for wild pokemon
        val wildLevel = Random.nextInt(minLevel, maxLevel)

        TODO("Complete once pokemon class is complete")
        enemyPokemon = Pokemon()
    }

    // throw pokeball to attempt catch
    fun throwPokeball(): Boolean {
        val sucess = playerTrainer.catchPokemon(playerPokemon, enemyPokemon)
        // boolean indicating catch success or fail
        return sucess;
    }

    override fun playerAttack(move: Move) {
        TODO("waiting for move class")
    }
}