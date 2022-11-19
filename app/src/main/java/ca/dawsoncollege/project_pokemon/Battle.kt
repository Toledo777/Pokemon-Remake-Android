package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random

abstract class Battle(val playerTrainer: PlayerTrainer) {
    // current pokemons in battle
    lateinit var playerPokemon: Pokemon
    lateinit var enemyPokemon: Pokemon

    // player chooses which pokemon to battle with
    fun chosePokemon(chosenPokemon: Pokemon) {
        if (chosenPokemon.hp > 0) {
            playerPokemon = chosenPokemon
        }
        else {
            throw IllegalArgumentException("Error, you cannot play a pokemon with 0 HP")
        }
    }

    fun switchOutPlayerPkm(nextPokemon: Pokemon) {
        if (nextPokemon.hp > 0) {
            playerPokemon = nextPokemon
        }
        else {
            throw IllegalArgumentException("Error, Cannot switch to a pokemon with 0 HP")
        }
    }

    // takes move as input and attacks enemy
    abstract fun playerAttack(move: Input)

    // chose random move to play for enemy, returns success status
    fun enemyAttack() {
        val moveList = this.enemyPokemon.moveList
        val move = Random.nextInt(0, 3);

        if (moveList[i].target == Target.HOSTILE) {
            if (moveSuccessCheck(moveList[i].accuracy)) {


            }

        }
    }

    // leave battle
    fun playerRun() {
        TODO("No sure if necessary")
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
}
