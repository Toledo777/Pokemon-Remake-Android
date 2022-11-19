package ca.dawsoncollege.project_pokemon

class Move (
    val name: String,
    var accuracy: Double,
    val ailmentChance: Double,
    val maxPP: Int,
    var power: Double,
    var heal: Double,
    var damageClass: String,
    val type: String,
    var target: String,
//    val ailment: String, TODO: more details in milestone 2
    ) {
    var PP: Int = 0;
    init {
        this.PP = this.maxPP
    }
}