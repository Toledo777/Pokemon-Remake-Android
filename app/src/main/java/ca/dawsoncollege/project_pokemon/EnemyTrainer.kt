package ca.dawsoncollege.project_pokemon

// optional trainer level, only used for enemy trainers
class EnemyTrainer(val name: String = "Ash Ketchum") {
    // battle team
    val MAX_TEAM_CAPACITY = 6
    // set team capacity to 6
    val team: ArrayList<Pokemon> = ArrayList(MAX_TEAM_CAPACITY)
}