package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class Battle(playerTrainer: PlayerTrainer, enemyTrainer: Trainer) {
    // current pokemons in battle
    var playerPokemon: Pokemon
    var ennemyPokemon: Pokemon
    // takes move as input and attacks ennemy
    fun playerAttack(move: Input) {

    }

    // chose random to target player with
    fun ennemyAttack() {

    }

    // leave battle
    fun playerRun() {

    }

}
