package ca.dawsoncollege.project_pokemon

import kotlin.math.pow

// TO ADD:
// Moves ArrayList
// Status
class Pokemon(var level: Int, var species: String, var name: String? = null) {
    var experience: Double = 0.0

    init {
        this.species = this.species.lowercase()
        this.name = if (this.name == null) this.species else this.name!!.lowercase()
        this.experience = this.level.toDouble().pow(3.0)
    }

    fun getBattleStats(): BattleStats? {
        return null
    }
}

data class PokemonData(
    val baseExperienceReward: Double,
    val baseStateAttack: Double,
    val baseStatDefense: Double,
    val baseStateMaxHp: Double,
    val baseStatSpecialAttack: Double,
    val baseStatSpecialDefense: Double,
    val baseStatSpeed: Double,
    val species: String,
    val types: List<String>
)

data class BattleStats(
    val maxHP: Int,
    var attack: Double,
    var defense: Double,
    var specialAttack: Double,
    var specialDefence: Double,
    var speed: Double
)