package ca.dawsoncollege.project_pokemon

// optional trainer level, only used for enemy trainers
open class Trainer(val trainerLevel: Int? = null, val name: String) {
    //
    protected val MAX_TEAM_CAPACITY = 6
    // set team capacity to 6
    protected val team: ArrayList<Pokemon> = ArrayList(MAX_TEAM_CAPACITY)
    // used to generate pokemons for enemy trainer
}