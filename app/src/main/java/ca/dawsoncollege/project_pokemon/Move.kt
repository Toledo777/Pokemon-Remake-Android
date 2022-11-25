package ca.dawsoncollege.project_pokemon

class Move (
    val name: String,
    var accuracy: Int,
    val ailmentChance: Int,
    val maxPP: Int,
    var power: Int,
    var heal: Int,
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