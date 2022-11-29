package ca.dawsoncollege.project_pokemon

import kotlin.random.Random

// optional trainer level, only used for enemy trainers
class EnemyTrainer(val name: String = "Ash Ketchum") {
    // battle team
    val MAX_TEAM_CAPACITY = 6
    // set team capacity to 6
    val team: ArrayList<Pokemon> = ArrayList(MAX_TEAM_CAPACITY)

    // generate team based on trainers level
    fun generateTeam(playerTrainer: PlayerTrainer) {
        for (i in 0 .. Random.nextInt(1, 6)) {
            // creates and adds a random pokemon within a random level within the range
            this.team.add(Pokemon(playerTrainer.getRandomEnemyLevel()))
        }
    }


}