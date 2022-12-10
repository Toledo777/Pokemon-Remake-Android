package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class ItemsFragment : Fragment() {
    private lateinit var battle: Battle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = arguments
        val battleJSON = data!!.getString("battle").toString()
        this.battle = convertJSONToWildBattle(battleJSON)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_items, container, false).rootView
        // set listener for potion button
        view.findViewById<Button>(R.id.potion_button).setOnClickListener {
            this.battle.playerUsePotion()
            this.battle.updatePlayerPokemon()
            val listener = activity as Callbacks
            listener.updateBattleText(this.battle.playerTrainer.playerName +" "+getString(R.string.use_potion))
            listener.updateHPUI(this.battle)
        }
        // set listener for pokeball button
        view.findViewById<Button>(R.id.pokeball_button).setOnClickListener {
            // TODO: check if battle = WildBattle first
            val listener = activity as Callbacks
            listener.updateBattleText(getString(R.string.throw_pokeball))
            val wild = this.battle as WildBattle
            // if captured
            if (wild.throwPokeball()){
//                Toast.makeText(context, "${this.battle.enemyPokemon.name} has been captured!", Toast.LENGTH_SHORT).show()
                listener.updateBattleText(getString(R.string.capture_success))
                listener.updateTeam(this.battle)
            }
            else {
                listener.updateBattleText(getString(R.string.capture_fail))
            }
        }
        return view
    }
}