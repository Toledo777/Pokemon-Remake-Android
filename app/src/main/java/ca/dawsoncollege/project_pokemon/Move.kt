package ca.dawsoncollege.project_pokemon

class Move (
    var accuracy: Double,
    var PP: Int,
    val maxPP: Int,
    var power: Double,
    var heal: Double,
//    var damage: Damage, TODO: damage class
    val type: Type,
    var target: Pokemon,
//    val ailment: String, TODO: more details in milestone 2
//    val ailmentChance: Double,
    )