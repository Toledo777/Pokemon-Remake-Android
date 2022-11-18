package ca.dawsoncollege.project_pokemon

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class PlayerTrainer: Trainer() {

    private val pokemonCollection: ArrayList<Pokemon> = ArrayList();
    // pokemon center

    // heal all pokemon
    fun pokemonCenterHeal() {

    }

    // send pokemon from team to box
    fun sendToBox() {

    }

    // catch wild pokemon
    fun catchPokemon() {

    }

    // switch pokemon on team with one in collection
    fun changeTeam(oldPokemon: Pokemon, newPokemonIndex: Int) {
        // send pokemon from team to box
        pokemonCollection.add(oldPokemon);
        this.team
    }
}