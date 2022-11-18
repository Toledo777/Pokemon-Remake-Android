package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlin.random.Random

class TrainerBattle(playerTrainer: PlayerTrainer, enemyTrainer: Trainer): Battle(playerTrainer) {

    // switch out enemy's pokemon when it has reached 0 hp
    fun switchOutEnemyPkm() {

    }

    override fun enemyAttack() {
        val move = Random.nextInt(0, 3);
        TODO("Waiting for move class")
    }

    override fun playerAttack(move: Move) {
        TODO("Waiting for move class")
    }

}