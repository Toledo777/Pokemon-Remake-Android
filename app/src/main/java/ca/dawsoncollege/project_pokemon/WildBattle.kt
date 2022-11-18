package ca.dawsoncollege.project_pokemon

class WildBattle(playerTrainer: PlayerTrainer): Battle(playerTrainer) {

    // generates random wild pokemon depending on trainer level
    fun generateWildPokemon() {
        val level = playerTrainer
    }

    fun throwPokeball(): Boolean {
        val sucess = playerTrainer.catchPokemon(playerPokemon, enemyPokemon)
        // boolean indicating catch success or fail
        return sucess;
    }
}