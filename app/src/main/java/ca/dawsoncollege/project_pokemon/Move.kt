package ca.dawsoncollege.project_pokemon

class Move (
    val accuracy: Double,
    var PP: Int,
    val maxPP: Int,
    val power: Double,
    val heal: Double,
//    var damage: Damage, TODO: damage class
//    val type: Type, TODO: type data class
    var target: Pokemon,
//    val ailment: String, TODO: more details in milestone 2
//    val ailmentChance: Double,
    )