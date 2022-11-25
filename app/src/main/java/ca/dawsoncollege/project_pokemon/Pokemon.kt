package ca.dawsoncollege.project_pokemon

import android.content.Context
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.floor
import kotlin.math.pow

// TO ADD:
// Status
// Fill moves array
class Pokemon(var context: Context, var level: Int, var species: String, var name: String? = null) {
    var data: PokemonData
    var battleStat: BattleStats
    var experience: Int = 0
    var hp: Int = 0
    var types: List<String>
    val NUMBER_OF_MOVES = 4
    var moveList: ArrayList<Move> = ArrayList(NUMBER_OF_MOVES)

    init {
        this.species = this.species.lowercase()
        this.name = if (this.name == null) this.species else this.name!!.lowercase()
        this.experience = this.level.toDouble().pow(3.0).toInt()
        runBlocking {
            val pokemonData = async { getPokemonData() }
            this@Pokemon.data = pokemonData.await()
        }
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

    // Get pokemon data from PokeAPI
    private suspend fun getPokemonData(): PokemonData {
        val pokemonData = getApiData(this.species)
        val stats = pokemonData!!.stats
        return PokemonData(
            pokemonData.base_experience,
            stats.find { it.stat.name == "attack" }!!.base_stat,
            stats.find { it.stat.name == "defense" }!!.base_stat,
            stats.find { it.stat.name == "hp" }!!.base_stat,
            stats.find { it.stat.name == "special-attack" }!!.base_stat,
            stats.find { it.stat.name == "special-defense" }!!.base_stat,
            stats.find { it.stat.name == "speed" }!!.base_stat,
            pokemonData.types.map { it.type.name }
        )
    }

    private suspend fun getApiData(species: String): ApiPokemonData? {
        val response = RetrofitInstance.api.getPokemon(species)
        return if (response.isSuccessful) {
            response.body()
        } else {
            throw Error("Error! There was a problem.")
        }
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

    private fun calcBattleStat(stat: Int, isMaxHp: Boolean = false): Int {
        val addedVal = if (isMaxHp) (this.level + 10) else 5
        val calculatedStat = floor(((((stat + 10) * this.level) / 50).toDouble())) + addedVal
        return calculatedStat.toInt()
    }
}

data class MoveLevel(val move: String, val level: Int)

data class PokemonData(
    val baseExperienceReward: Int,
    val baseStateAttack: Int,
    val baseStatDefense: Int,
    val baseStateMaxHp: Int,
    val baseStatSpecialAttack: Int,
    val baseStatSpecialDefense: Int,
    val baseStatSpeed: Int,
    val types: List<String>
)

data class BattleStats(
    var maxHP: Int,
    var attack: Int,
    var defense: Int,
    var specialAttack: Int,
    var specialDefence: Int,
    var speed: Int
)