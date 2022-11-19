package ca.dawsoncollege.project_pokemon

class Move (
    var accuracy: Double,
    var PP: Int,
    val maxPP: Int,
    var power: Double,
    var heal: Double,
    var damage: DamageClass,
    val type: Type,
    // may be changed later
    var target: Target,
//    val ailment: String, TODO: more details in milestone 2
//    val ailmentChance: Double,
    )
enum class Target{
    FRIENDLY, HOSTILE
}

enum class DamageClass {
    PHYSICAL, SPECIAL
}