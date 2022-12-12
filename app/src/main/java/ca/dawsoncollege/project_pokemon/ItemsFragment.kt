package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ItemsFragment : Fragment() {
    private lateinit var battle: Battle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = arguments
        val battleJSON = data!!.getString("battle").toString()
        val battleType = data.getString("type").toString()
        if (battleType == "wild")
            this.battle = convertJSONToWildBattle(battleJSON)
        else
            this.battle = convertJSONToTrainerBattle(battleJSON)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_items, container, false).rootView
        val listener = activity as Callbacks
        // set listener for potion button
        view.findViewById<Button>(R.id.potion_button).setOnClickListener {
            this.battle.playerUsePotion()
            this.battle.updatePlayerPokemon()
            listener.updateBattleText(this.battle.playerTrainer.playerName +" "+getString(R.string.use_potion) + " on " +this.battle.playerPokemon.name)
            listener.updateHPUI(this.battle)
            lifecycleScope.launch(Dispatchers.Main) {
                performEnemyMove(this@ItemsFragment.battle, listener)
                listener.updateHPUI(this@ItemsFragment.battle)
            }
        }
        // set listener for pokeball button
        view.findViewById<Button>(R.id.pokeball_button).setOnClickListener {
            if (this.battle is WildBattle){
                // if captured
                if ((this.battle as WildBattle).throwPokeball()){
                    listener.updateBattleText(this.battle.enemyPokemon.name +" "+ getString(R.string.capture_success))
                    listener.updateTeam(this.battle)
                    listener.capturedPokemon(this.battle)
                }
                else
                    listener.updateBattleText(getString(R.string.capture_fail))
            } else
                listener.updateBattleText(getString(R.string.capture_disabled))
        }
        return view
    }
}