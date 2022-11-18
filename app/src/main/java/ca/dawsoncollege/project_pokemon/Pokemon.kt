package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// TO ADD:
// Moves ArrayList
// Status
class Pokemon(
    val species: String,
    var name: String,
    var level: Int,
    var experience: Double,
    var baseExp: Double,
    val types: ArrayList<String>,
    var hp: Int,
) {
    fun getBattleStats(): BattleStats? {
        return null
    }
}

data class BattleStats(
    val maxHP: Int,
    var attack: Double,
    var defense: Double,
    var specialAttack: Double,
    var specialDefence: Double,
    var speed: Double
)