package ca.dawsoncollege.project_pokemon

// TO ADD:
// Moves ArrayList
// Status
class Pokemon(
    val species: String,
    var name: String,
    var level: Int,
    var experience: Double,
    var baseExp: Double,
    val types: ArrayList<String>,
    val hp: Int,
) {
    fun getBattleStats() {

    }
}

data class BattleStats(
    val maxHP: Int,
    var attack: Double,
    var defense: Double,
    var specialAttack: Double,
    var specialDefence: Double,
    var speed: Double
)