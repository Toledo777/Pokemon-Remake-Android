package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// optional trainer level, only used for enemy trainers
open class Trainer(val trainerLevel: Int? = null, val name: String) {
    //
    protected val MAX_TEAM_CAPACITY = 6
    // set team capacity to 6
    val team: ArrayList<Pokemon> = ArrayList(MAX_TEAM_CAPACITY)
    // used to generate pokemons for enemy trainer
}