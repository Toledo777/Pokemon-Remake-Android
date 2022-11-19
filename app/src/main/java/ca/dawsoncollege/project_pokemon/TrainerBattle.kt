package ca.dawsoncollege.project_pokemon


import android.content.Context
import kotlin.random.Random

class TrainerBattle(playerTrainer: PlayerTrainer, private val enemyTrainer: Trainer, context: Context): Battle(playerTrainer, context) {

    init{
        // set enemy trainer's current pokemon
        for (i in 0 .. Random.nextInt(1, 6)) {
            enemyTrainer.team.add(Pokemon(context, playerTrainer.getRandomEnemyLevel(), "caterpie"))
        }
        this.enemyPokemon = enemyTrainer.team[0]
    }

    // switch out enemy's pokemon when it has reached 0 hp
    fun switchOutEnemyPkm() {
        // remove fainted pokemon from team
        enemyTrainer.team.remove(enemyPokemon)
        // set next pokemon as enemy
        enemyPokemon = enemyTrainer.team[0];
    }
}