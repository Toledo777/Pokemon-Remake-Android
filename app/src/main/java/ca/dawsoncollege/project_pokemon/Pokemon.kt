package ca.dawsoncollege.project_pokemon

import android.content.Context
import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlin.math.floor
import kotlin.math.pow
import kotlin.random.Random

// TO ADD:
// Status
class Pokemon(
    var context: Context,
    var level: Int,
    var species: String? = null,
    var name: String? = null
) {
    var data: PokemonData
    var battleStat: BattleStats
    var experience: Int = 0
    var hp: Int = 0
    var types: List<String>
    val NUMBER_OF_MOVES = 4
    var moveList: ArrayList<Move> = ArrayList(NUMBER_OF_MOVES)

    init {
        runBlocking {
            if (this@Pokemon.species == null) {
                // If no pokemon species is provided, generate the id of a random GEN 1 pokemon
                val id = Random.nextInt(0, 152).toString()
                val pokemonData = async { getPokemonData(id) }
                this@Pokemon.data = pokemonData.await()
                this@Pokemon.species = this@Pokemon.data.species
            } else {
                // Get pokemon data from API based on the given species
                this@Pokemon.species = this@Pokemon.species!!.lowercase()
                val pokemonData = async { getPokemonData(this@Pokemon.species!!) }
                this@Pokemon.data = pokemonData.await()
            }
        }
        this.name = if (this.name == null) this.species else this.name!!.lowercase()
        this.types = this.data.types
        this.hp = this.data.baseStateMaxHp
        this.experience = this.level.toDouble().pow(3.0).toInt()
        this.battleStat = getBattleStats()
    }

    // Add initial moves
    private suspend fun addMoves(data: ApiPokemonData) {
        // Get all possible moves based on the Pokemon's current level
        val availableMoves = getAllPossibleMoves(data).filter { it.level <= this.level }

        // Add to list directly ff there are only 4 moves available
        if (availableMoves.size <= NUMBER_OF_MOVES) {
            val moves = availableMoves.map {
                val details = getApiMove(it.move)
                createMove(details)
            }
            this.moveList.addAll(moves)
        } else {
            // Add four random moves from the available moves
            val moves = availableMoves.shuffled().take(NUMBER_OF_MOVES).map {
                val details = getApiMove(it.move)
                createMove(details)
            }
            this.moveList.addAll(moves)
        }

    }

    // Get all potential moves for the Pokemon (Name and level)
    private fun getAllPossibleMoves(data: ApiPokemonData): List<MoveLevel> {
        return data.moves.map {
            val name = it.move.name
            val level = it.version_group_details[0].level_learned_at
            MoveLevel(name, level)
        }
    }

    // Create Move object using API data
    private fun createMove(details: ApiMoveDetails?): Move {
        return Move(
            details!!.name,
            details.accuracy,
            details.meta.ailment_chance,
            details.pp,
            details.power,
            details.meta.healing,
            details.damage_class.name.uppercase(),
            details.type.name,
            if (details.target.name == "selected-pokemon") "OPPONENT" else "USER",
            details.meta.ailment.name
        )
    }

    // Get pokemon data from PokeAPI
    private suspend fun getPokemonData(speciesOrId: String): PokemonData {
        val pokemonData = getApiPokemon(speciesOrId)

        // Add initial moves
        addMoves(pokemonData!!)

        val stats = pokemonData.stats
        return PokemonData(
            pokemonData.species.name,
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

    // Get data for a specific move from the API
    private suspend fun getApiMove(name: String): ApiMoveDetails? {
        val response = RetrofitInstance.api.getMove(name)
        return if (response.isSuccessful) {
            response.body()
        } else {
            throw Error("Error! There was a problem.")
        }
    }

    // Get data for a specific pokemon from the API
    private suspend fun getApiPokemon(species: String): ApiPokemonData? {
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

    // method to add exp to pokemon, updates level accordingly
    fun addExp(exp: Int) {
        // add exp
        this.experience += exp
        // update level
        this.level = floor(this.experience.toDouble().pow(1 / 3)).toInt()

    }
}

data class MoveLevel(val move: String, val level: Int)

data class PokemonData(
    val species: String,
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