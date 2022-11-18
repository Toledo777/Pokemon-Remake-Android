package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.math.floor
import kotlin.math.pow

// TO ADD:
// Moves ArrayList
// Status
class Pokemon(var context: Context, var level: Int, var species: String, var name: String? = null) {
    var data: PokemonData
    lateinit var battleStat: BattleStats
    var experience: Double = 0.0
    var hp: Double = 0.0
    lateinit var types: List<String>

    init {
        this.species = this.species.lowercase()
        this.name = if (this.name == null) this.species else this.name!!.lowercase()
        this.experience = this.level.toDouble().pow(3.0)
        this.data = getPokemonData()
        this.types = this.data.types
        this.hp = this.data.baseStateMaxHp
        this.battleStat = getBattleStats()
    }

    private fun getPokemonData(): PokemonData {
        return JSON.getJsonData(
            this.context,
            "pokemon/${this.species}.json",
            PokemonData::class.java
        ) as PokemonData
    }

    fun getBattleStats(): BattleStats {
        return BattleStats(
            floor((((this.data.baseStateMaxHp + 10) * this.level) / 50)) + this.level + 10,
            floor((((this.data.baseStateAttack + 10) * this.level) / 50)) + 5,
            floor((((this.data.baseStatDefense + 10) * this.level) / 50)) + 5,
            floor((((this.data.baseStatSpecialAttack + 10) * this.level) / 50)) + 5,
            floor((((this.data.baseStatSpecialDefense + 10) * this.level) / 50)) + 5,
            floor((((this.data.baseStatSpeed + 10) * this.level) / 50)) + 5,
        )
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
    val maxHP: Double,
    var attack: Double,
    var defense: Double,
    var specialAttack: Double,
    var specialDefence: Double,
    var speed: Double
)