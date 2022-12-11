package ca.dawsoncollege.project_pokemon

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MovesFragment : Fragment() {
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
        val view = inflater.inflate(R.layout.fragment_moves, container, false).rootView
        val buttons = arrayListOf<Button>(
            view.findViewById(R.id.move_1),
            view.findViewById(R.id.move_2),
            view.findViewById(R.id.move_3),
            view.findViewById(R.id.move_4))
            setMoves(buttons)
        return view
    }

    // set move button text and on click listener
    private fun setMoves(buttons: ArrayList<Button>){
        val moveList = this.battle.playerPokemon.moveList
        for (i in 0 until this.battle.playerPokemon.NUMBER_OF_MOVES){
            val moveButtonText = "${moveList[i].name.replace('-', ' ')}\n" +
                    "${moveList[i].PP}/${moveList[i].maxPP}\n${moveList[i].type}"

            val listener = activity as Callbacks
            // set text
            buttons[i].text = moveButtonText
            // set listener
            buttons[i].setOnClickListener {
                // if move has remaining PP
                if (moveList[i].PP > 0){
                    // if player pokemon is not fainted
                    if (this.battle.playerPokemon.hp != 0) {

                        // specific dispatcher is specified in the functions involved
                        lifecycleScope.launch(Dispatchers.Main){
                            playTurn(moveList, buttons, i)
                            Log.d("EXTENSION", "enemy: "+battle.enemyPokemon.hp.toString())
                            Log.d("EXTENSION", "player: "+battle.playerPokemon.hp.toString())
                            // callback to update HP UI in BattleActivity
                            listener.updateHPUI(this@MovesFragment.battle)
                        }
                    } else {
//                        Toast.makeText(context, "${this.battle.playerPokemon.name} is fainted!", Toast.LENGTH_SHORT).show()
                        this.battle.playerPokemon.name?.let { name -> listener.updateBattleText(name + " " + getString(R.string.fainted)) }
                    }
                } else {
//                    Toast.makeText(context, "Out of PP!", Toast.LENGTH_SHORT).show()
                    listener.updateBattleText(getString(R.string.pp_out))
                }
            }
        }
    }

    // update button text
    private fun updateMovePP(button: Button, move: Move) {
        val moveButtonText = "${move.name.replace('-', ' ')}\n" +
                "${move.PP}/${move.maxPP}\n${move.type}"
        button.text = moveButtonText
    }

    // play a turn
    private suspend fun playTurn(moveList: ArrayList<Move>, buttons: ArrayList<Button>, i: Int){
        // check who attacks first
        val listener = activity as Callbacks
        if (this.battle.playerPokemon.battleStat.speed >= this.battle.enemyPokemon.battleStat.speed){
            if (!this.battle.checkPokemonFainted()){
                // attempt move, set text, update pp and button
                performPlayerMove(moveList, buttons, i, listener)
                this.battle = performEnemyMove(this.battle, listener)
            } else { //TODO: might not need this after end the battle properly
                this.battle.enemyPokemon.name?.let { name -> listener.updateBattleText(name + " " + getString(R.string.fainted)) }
            }
        } else {
            this.battle = performEnemyMove(this.battle, listener)
            // if player pokemon is not fainted
            performPlayerMove(moveList, buttons, i, listener)
        }
        // update player data
        this.battle.updatePlayerPokemon()
    }

    private suspend fun performPlayerMove(moveList: ArrayList<Move>, buttons: ArrayList<Button>, i: Int, listener: Callbacks){
        // if player pokemon is not fainted
        if (this.battle.playerPokemon.hp != 0){
            if (!this.battle.checkPokemonFainted()){
                playPlayerMove(moveList[i], buttons[i])
            }
            val oldLevel = this.battle.playerPokemon.level
            if (this.battle.checkPokemonFainted()){
            // TODO: end battle
                // Toast.makeText(context, "${this.battle.enemyPokemon.name} fainted!", Toast.LENGTH_SHORT).show()
                this.battle.enemyPokemon.name?.let { name -> listener.updateBattleText(name + " " + getString(R.string.fainted)) }
                if (this.battle.playerPokemon.level > oldLevel)
                    this.battle.playerPokemon.name?.let { name -> listener.updateBattleText(name + " " + getString(R.string.level_up)) }
            }
        } else {
//            Toast.makeText(context, "${this.battle.playerPokemon.name} fainted!", Toast.LENGTH_SHORT).show()
                this.battle.playerPokemon.name?.let { name -> listener.updateBattleText(name + " " + getString(R.string.fainted)) }
        }
    }

    // helper method, plays move and sets battle text for it
    private suspend fun playPlayerMove(move: Move, button: Button) {
        val listener = activity as Callbacks
        if(this.battle.playerMove(move)) {
            this.battle.playerPokemon.name?.let { listener.updateBattleText(it + " " + getString(R.string.used) + " " + move.name) }
        }
        // missed move
        else {
            this.battle.playerPokemon.name?.let { listener.updateBattleText(it + " " + getString(R.string.miss_move))}
        }
        Log.d("MOVES_FRAG", move.toString())
        move.PP -= 1
        updateMovePP(button, move)
    }
}

// extension function
suspend fun performEnemyMove(battle: Battle, listener: Callbacks): Battle{
    // if enemy pokemon is not fainted
    if (!battle.checkPokemonFainted()){
        // move success
        val moveName = battle.playEnemyMove()
        // move succed
        if (moveName != null) {
            battle.enemyPokemon.name?.let { listener.updateBattleText(it + " " + Resources.getSystem().getString(R.string.used) + " " + moveName) }
        }
        // move missed
        else {
            battle.enemyPokemon.name?.let { listener.updateBattleText(it + " " + Resources.getSystem().getString(R.string.miss_move)) }
        }
        if (battle.playerPokemon.hp == 0)
            battle.playerPokemon.name?.let { name -> listener.updateBattleText(name + " " + Resources.getSystem().getString(R.string.fainted)) }
    }
    return battle
}