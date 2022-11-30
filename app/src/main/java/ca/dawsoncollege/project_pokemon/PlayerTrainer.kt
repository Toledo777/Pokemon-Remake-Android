package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.random.Random


class PlayerTrainer(val playerName: String) {
    val MAX_TEAM_CAPACITY = 6
    val team: ArrayList<Pokemon> = ArrayList(MAX_TEAM_CAPACITY)
    private val pokemonCollection: ArrayList<Pokemon> = ArrayList()
    private val STARTER_LEVEL = 5

    // sets players starter pokemon
    fun setStarter(species: String, name: String? = null) {
        val starter = Pokemon(STARTER_LEVEL, species, name)

        this.team.add(starter)
    }
    // heal all pokemon
    // TO-DO cure status effects
    fun pokemonCenterHeal() {
        this.team.forEach {
            it.hp = it.battleStat.maxHP

            // restore all moves to max PP
            it.moveList.forEach { move ->
                move.PP = move.maxPP
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

    // boolean to get wild pokemon catch success or fail
    fun catchPokemon(playerPokemon: Pokemon, wildPokemon: Pokemon): Boolean {
        // calculate probability (in percentage?)
        val captureProb = 1 - (wildPokemon.hp / playerPokemon.battleStat.maxHP)
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
    private fun calculateMaxTeamLevel(): Int {
        var maxLevel = this.team[0].level;

        if (this.team.size > 1){
            for (i in 1 until team.size) {
                if (this.team[i].level > maxLevel)
                    maxLevel = this.team[i].level
            }
        }
        return maxLevel;
    }

    // return lowest level on player team
    private fun calculateMinTeamLevel(): Int {
        var minLevel = this.team[0].level;

        if (this.team.size > 1){
            for (i in 1 until team.size) {
                if (this.team[i].level < minLevel)
                    minLevel = this.team[i].level
            }
        }
        return minLevel;
    }

    // generates a random level for enemy pokemon that is in the correct range (depends on the trainers level)
    fun getRandomEnemyLevel(): Int {
        val minLevel = this.calculateMinTeamLevel()
        val maxLevel = this.calculateMaxTeamLevel()

        return if(minLevel != maxLevel){
            // return random level for enemy pokemon
            Random.nextInt(minLevel, maxLevel)
        } else {
            minLevel
        }
    }
}
