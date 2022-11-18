package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class Battle(playerTrainer: PlayerTrainer) {
    // current pokemons in battle
    lateinit var playerPokemon: Pokemon
    lateinit var enemyPokemon: Pokemon

    // player choses which pokemon to battle with
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

    // chose random to target player with
    abstract fun enemyAttack()

    // leave battle
    fun playerRun() {

    }

}
