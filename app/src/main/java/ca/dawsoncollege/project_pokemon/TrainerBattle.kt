package ca.dawsoncollege.project_pokemon


import kotlin.random.Random

class TrainerBattle(playerTrainer: PlayerTrainer, val enemyTrainer: Trainer): Battle(playerTrainer) {

    init{
        // set enemy trainer's current pokemon
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