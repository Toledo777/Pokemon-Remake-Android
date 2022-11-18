package ca.dawsoncollege.project_pokemon

class PlayerTrainer(private val playerName: String): Trainer(null, playerName) {

    private val pokemonCollection: ArrayList<Pokemon> = ArrayList()
    // pokemon center

    // heal all pokemon
    fun pokemonCenterHeal() {

    }

    // send pokemon from team to box
    fun sendToBox() {

    }

    // catch wild pokemon
    fun catchPokemon() {

    }

    // switch pokemon on team with one in collection
    // NOTE: The new pokemon is gotten using index for now, it can be changed later
    // to get a pokemon object if more convenient
    fun changeTeam(oldPokemon: Pokemon, newPokemonIndex: Int) {
        // send pokemon from team to box
        this.team.remove(oldPokemon)
        // add team pokemon to box
        pokemonCollection.add(oldPokemon)

        // remove from box
        val newPokemon = pokemonCollection.removeAt(newPokemonIndex)
        // add to team
        this.team.add(newPokemon)
    }
}
