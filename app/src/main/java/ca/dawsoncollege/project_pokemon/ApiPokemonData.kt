package ca.dawsoncollege.project_pokemon

data class ApiPokemonData(
    val species: Species,
    val base_experience: Int,
    val stats: List<BaseStat>,
    val moves: List<ApiMove>,
    val types: List<ApiType>,
    val sprites: Sprite
)

data class ApiMoveDetails (
    val name: String,
    val accuracy: Int,
    val meta: Meta,
    val pp: Int,
    val power: Int,
    val damage_class: DamageClass,
    val type: MoveType,
    val target: Target,
)

// Move details
data class Meta(val ailment: Ailment, val ailment_chance: Int, val healing: Int)

data class Ailment(val name: String, val url: String)

data class DamageClass(val name: String, val url: String)

data class MoveType(val name: String, val url: String)

data class Target(val name: String, val url: String)

// Species
data class Species(val name: String)

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