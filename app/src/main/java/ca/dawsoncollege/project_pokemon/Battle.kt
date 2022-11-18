package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class Battle(playerTrainer: PlayerTrainer) {
    // current pokemons in battle
    lateinit var playerPokemon: Pokemon
    lateinit var enemyPokemon: Pokemon

    // player choses which pokemon to battle with
    fun chosePokemon(chosenPokemon: Pokemon) {
        playerPokemon = chosenPokemon
    }
    // takes move as input and attacks enemy
    abstract fun playerAttack(move: Input)

    // chose random to target player with
    abstract fun enemyAttack() {

    }

    // leave battle
    fun playerRun() {

    }

}
