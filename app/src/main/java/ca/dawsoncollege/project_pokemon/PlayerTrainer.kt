package ca.dawsoncollege.project_pokemon

class PlayerTrainer(private val playerName: String): Trainer(null, playerName) {

    private val pokemonCollection: ArrayList<Pokemon> = ArrayList();
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
    fun changeTeam(oldPokemon: Pokemon, newPokemonIndex: Int) {
        // send pokemon from team to box
        this.team.remove(oldPokemon);
        pokemonCollection.add(oldPokemon);

    }
}
