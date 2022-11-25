package ca.dawsoncollege.project_pokemon

data class ApiPokemonData(
    val base_experience: Int,
    val stats: List<BaseStat>,
    val moves: List<ApiMove>,
    val types: List<ApiType>,
    val sprites: Sprite
)

// BaseStats
data class BaseStat(val base_stat: Int, val stat: Stat)

data class Stat(val name: String)

// Move
data class ApiMove(val move: MoveData, val version_group_details: List<LevelLearned>)

data class MoveData(val name: String, val url: String)

data class LevelLearned(val level_learned_at: Int)

// Type
data class ApiType(val slot: Int, val type: TypeData)

data class TypeData(val name: String, val url: String)

// Sprite
data class Sprite(val back_default: String, val front_Default: String)