package ca.dawsoncollege.project_pokemon
// create an variable of Move type
class Move(
    val name: String,
    var accuracy: Int,
    val ailmentChance: Int,
    val maxPP: Int,
    var power: Int,
    var heal: Int,
    var damageClass: String,
    val type: String,
    var target: String,
    val ailment: String,
) {
    var PP: Int = 0;

    init {
        this.PP = this.maxPP
    }

    override fun toString(): String {
        return "Name: $name | Type: $type | Power: $power | Class: $damageClass | Target: $target"
    }

}