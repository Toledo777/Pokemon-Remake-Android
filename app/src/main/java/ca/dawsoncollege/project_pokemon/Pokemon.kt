package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlin.math.floor
import kotlin.math.pow

// TO ADD:
// Status
// Fill moves array
class Pokemon(var context: Context, var level: Int, var species: String, var name: String? = null) {
    var data: PokemonData
    var battleStat: BattleStats
    var experience: Double = 0.0
    var hp: Double = 0.0
    var types: List<String>
    val NUMBER_OF_MOVES = 4
    var moveList: ArrayList<Move> = ArrayList(NUMBER_OF_MOVES)

    init {
        this.species = this.species.lowercase()
        this.name = if (this.name == null) this.species else this.name!!.lowercase()
        this.experience = this.level.toDouble().pow(3.0)
        this.data = getPokemonData()
        this.types = this.data.types
        this.hp = this.data.baseStateMaxHp
        this.battleStat = getBattleStats()
        addMoves()
    }

    // Add initial moves
    private fun addMoves() {
        val moves = (JSON.getJsonData(
            context,
            "move_lists/${this.species}.json",
            Array<MoveLevel>::class.java
        ) as Array<*>).toList()
        moves
            .sortedByDescending { (it as MoveLevel).level }
            .forEach {
                it as MoveLevel
                if (moveList.count() < NUMBER_OF_MOVES && it.level <= this.level) {
                    moveList.add(getMoveInfo(it))
                } else if (moveList.count() == NUMBER_OF_MOVES) {
                    return
                }
            }
    }

    // Get move data from JSON
    private fun getMoveInfo(move: MoveLevel): Move {
        return JSON.getJsonData(this.context, "moves/${move.move}.json", Move::class.java) as Move
    }

    // Get pokemon data from JSON
    private fun getPokemonData(): PokemonData {
        return JSON.getJsonData(
            this.context,
            "pokemon/${this.species}.json",
            PokemonData::class.java
        ) as PokemonData
    }

    fun getBattleStats(): BattleStats {
        return BattleStats(
            calcBattleStat(this.data.baseStateMaxHp, true),
            calcBattleStat(this.data.baseStateAttack),
            calcBattleStat(this.data.baseStatDefense),
            calcBattleStat(this.data.baseStatSpecialAttack),
            calcBattleStat(this.data.baseStatSpecialDefense),
            calcBattleStat(this.data.baseStatSpeed),
        )
    }

    private fun calcBattleStat(stat: Double, isMaxHp: Boolean = false): Double {
        val addedVal = if (isMaxHp) (this.level + 10) else 5
        return floor((((stat + 10) * this.level) / 50)) + addedVal
    }

    // method to add exp to pokemon, updates level accordingly
    fun addExp(exp: Int) {
        // add exp
        this.experience += exp
        // update level
        this.level = floor(this.experience.pow(1/3)).toInt()

    }
}

data class MoveLevel(val move: String, val level: Int)

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