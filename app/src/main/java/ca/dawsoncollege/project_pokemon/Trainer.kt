package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

// optional trainer level, only used for enemy trainers
class Trainer(val trainerLevel: int = null, val name: String) {
    //
    private val MAX_TEAM_CAPACITY = 6;
    // set team capacity to 6
    private var team: ArrayList<Pokemon> = ArrayList(MAX_TEAM_CAPACITY);
    // used to generate pokemons for enemy trainer
}