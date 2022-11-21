package ca.dawsoncollege.project_pokemon

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random

abstract class Battle(val playerTrainer: PlayerTrainer, val context: Context) {
    // current pokemons in battle
    var playerPokemon = playerTrainer.team[0]
    lateinit var enemyPokemon: Pokemon

    //
    // player chooses which pokemon to battle with
//    fun chosePokemon(chosenPokemon: Pokemon) {
//        if (chosenPokemon.hp > 0) {
//            playerPokemon = chosenPokemon
//        }
//        else {
//            throw IllegalArgumentException("Error, you cannot play a pokemon with 0 HP")
//        }
//    }

    fun switchOutPlayerPkm(nextPokemon: Pokemon) {
        if (nextPokemon.hp > 0) {
            playerPokemon = nextPokemon
        }
        else {
            throw IllegalArgumentException("Error, Cannot switch to a pokemon with 0 HP")
        }
    }

    // TODO Potentially create method for friendly player moves
    // takes move as input and attempts attacks on enemy
    fun playerAttack(move: Move): Boolean {
        if (moveSuccessCheck(move.accuracy)) {
            val damage = calculateDamage(move, playerPokemon, enemyPokemon)
            enemyPokemon.hp -= damage
            return true
        }
        // moves missed
        return false
    }

    // chose random move to play for enemy, returns success status
    fun enemyAttack(): Boolean{
        val moveList = this.enemyPokemon.moveList
        val moveIndex = Random.nextInt(0, 3);

        // hostile move
        if (moveList[moveIndex].target == "HOSTILE") {
            return if (moveSuccessCheck(moveList[moveIndex].accuracy)) {
                val damage = calculateDamage(moveList[moveIndex], enemyPokemon, playerPokemon)
                // subtract hp
                playerPokemon.hp -= damage
                true
            }
            // move missed
            else {
                false
            }
        }
        TODO("Implement friendly moves")
    }

    // leave battle
    fun playerRun() {
        TODO("Not sure if necessary")
    }

    // check if move succeeds
    private fun moveSuccessCheck(accuracy: Double): Boolean {
        val randNum = Random.nextDouble(0.0, 100.0)
        // check move success using probabilities
        if (accuracy >= randNum) {
            // move succeeds
            return true;
        }
        // move fails
        return false;
    }

    // TODO add type multiplier in milestone 2
    // helper method to calculate physical damage of an attack
    private fun calculateDamage(move: Move, attacker: Pokemon, defender: Pokemon): Int{
        var damage: Double = ((2*attacker.level / 5) + 2) / 50.0
        damage *= move.power

        damage = if (move.damageClass == "PHYSICAL")
            // physical
            damage * (attacker.getBattleStats().attack / defender.getBattleStats().defense) + 2
        else
            // special
            damage * (attacker.getBattleStats().specialAttack / defender.getBattleStats().specialAttack) + 2

        // return damage as an int
        return damage.toInt()
    }
}
