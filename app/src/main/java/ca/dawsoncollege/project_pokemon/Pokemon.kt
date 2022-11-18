package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.math.pow

// TO ADD:
// Moves ArrayList
// Status
class Pokemon(var context: Context, var level: Int, var species: String, var name: String? = null) {
    var data: PokemonData
    var experience: Double = 0.0

    init {
        this.species = this.species.lowercase()
        this.name = if (this.name == null) this.species else this.name!!.lowercase()
        this.experience = this.level.toDouble().pow(3.0)
        this.data = getPokemonData()
    }

    private fun getPokemonData(): PokemonData {
        return JSON.getJsonData(this.context, "pokemon/${this.species}.json", PokemonData::class.java) as PokemonData
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