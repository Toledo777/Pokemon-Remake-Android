package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.math.floor
import kotlin.math.pow

// TO ADD:
// Moves ArrayList
// Status
class Pokemon(var context: Context, var level: Int, var species: String, var name: String? = null) {
    var data: PokemonData
    var battleStat: BattleStats
    var experience: Double = 0.0
    var hp: Double = 0.0
    var types: List<String>
    val NUMBER_OF_MOVES = 4;
    var moveList: ArrayList<Move> = ArrayList(NUMBER_OF_MOVES)

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
            calcBattleStat(this.data.baseStateAttack),
            calcBattleStat(this.data.baseStatDefense),
            calcBattleStat(this.data.baseStatSpecialAttack),
            calcBattleStat(this.data.baseStatSpecialDefense),
            calcBattleStat(this.data.baseStatSpeed),
        )
    }

    private fun calcBattleStat(stat: Double): Double {
        return floor((((stat + 10) * this.level) / 50)) + 5
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