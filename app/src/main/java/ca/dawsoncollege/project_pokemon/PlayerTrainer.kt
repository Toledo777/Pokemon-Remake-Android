package ca.dawsoncollege.project_pokemon

import android.content.Context
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.random.Random

object TrainerConstants {
    const val STARTER_LEVEL = 5;
    const val MAX_TEAM_CAPACITY= 6;
}

@Entity
data class PlayerTrainer(@PrimaryKey val playerName: String) {
    @ColumnInfo(name = "team") var team: ArrayList<Pokemon> = ArrayList(TrainerConstants.MAX_TEAM_CAPACITY)
    @ColumnInfo(name = "pokemon_collection") var pokemonCollection: ArrayList<Pokemon> = ArrayList()

    // sets players starter pokemon
    fun setStarter(species: String, name: String? = null) {
        val starter = Pokemon(TrainerConstants.STARTER_LEVEL, species, name)

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

    // boolean to get wild pokemon catch success or fail
    fun catchPokemon(playerPokemon: Pokemon, wildPokemon: Pokemon): Boolean {
        // calculate probability (in percentage?)
        val captureProb = 1 - (wildPokemon.hp / playerPokemon.battleStat.maxHP)
        val rand = Random.nextDouble(1.0);

        // capture is successful if rand num was less then capture prob
        if (captureProb >= rand) {
            // check if space on team
            if (this.team.size < TrainerConstants.MAX_TEAM_CAPACITY) {
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
