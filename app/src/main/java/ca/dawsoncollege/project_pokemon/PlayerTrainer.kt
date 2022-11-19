package ca.dawsoncollege.project_pokemon

import kotlin.random.Random

class PlayerTrainer(playerName: String): Trainer(null, playerName) {

    private val pokemonCollection: ArrayList<Pokemon> = ArrayList()
    // pokemon center

    // heal all pokemon
    fun pokemonCenterHeal() {
        this.team.forEach {
            it.hp = it.getBattleStats().maxHP
            // TO-DO cure status effects once they have been implemented in pokemon class
            this.team.forEach {

            }
        }
    }

    // TO-DO check if pokemon in box should be healed
    // send pokemon from team to box
    fun sendToBox(oldPokemon: Pokemon) {
        // send pokemon from team to box
        this.team.remove(oldPokemon)
        // add team pokemon to box
        pokemonCollection.add(oldPokemon)
    }

    // boolean to get wild pokemon catch sucess or fail
    fun catchPokemon(playerPokemon: Pokemon, wildPokemon: Pokemon): Boolean {
        // calculate probability (in percentage?)
        val captureProb = 1 - (wildPokemon.hp / playerPokemon.getBattleStats()?.maxHP!!)
        val rand = Random.nextDouble(1.0);

        // capture is successful if rand num was less then capture prob
        if (captureProb >= rand) {
            // check if space on team
            if (this.team.size < MAX_TEAM_CAPACITY) {
                this.team.add(wildPokemon);
            }
            // add to collection
            else {
                this.pokemonCollection.add(wildPokemon);
            }
            return true;
        }
        return false;
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

    // get highest level on players team
    fun calculateMaxTeamLevel(): Int {
        var maxLevel = this.team[0].level;

        for (i in 1..team.size) {
            if (this.team[i].level > maxLevel)
                maxLevel = this.team[i].level
        }
        return maxLevel;
    }

    // return lowest level on player team
    fun calculateMinTeamLevel(): Int {
        var minLevel = this.team[0].level;

        for (i in 1..team.size) {
            if (this.team[i].level < minLevel)
                minLevel = this.team[i].level
        }
        return minLevel;
    }
}
