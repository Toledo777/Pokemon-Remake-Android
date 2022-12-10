package ca.dawsoncollege.project_pokemon

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
        // TODO: check if wild or trainer
        this.battle = convertJSONToWildBattle(battleJSON)
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
                        lifecycleScope.launch{
                            playTurn(moveList, buttons, i)
                        }
                        // callback to update HP UI in BattleActivity
                        listener.updateHPUI(this.battle)
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

    // TODO: to optimize?
    // play a turn
    private suspend fun playTurn(moveList: ArrayList<Move>, buttons: ArrayList<Button>, i: Int){
        // check who attacks first
        if (this.battle.playerPokemon.battleStat.speed >= this.battle.enemyPokemon.battleStat.speed){
            this.battle.playerMove(moveList[i])
            Log.d("MOVES_FRAG", moveList[i].toString())
            moveList[i].PP -= 1
            updateMovePP(buttons[i], moveList[i])
            // if enemy pokemon is not fainted
            if (!this.battle.checkPokemonFainted())
                this.battle.playEnemyMove()
        } else {
            this.battle.playEnemyMove()
            // if player pokemon is not fainted
            if (this.battle.playerPokemon.hp != 0){
                this.battle.playerMove(moveList[i])
                Log.d("MOVES_FRAG", moveList[i].toString())
                moveList[i].PP -= 1
                updateMovePP(buttons[i], moveList[i])
                this.battle.checkPokemonFainted()
            } else {
                Toast.makeText(context, "${this.battle.playerPokemon.name} fainted!", Toast.LENGTH_SHORT).show()
            }
        }
        // update player data
        this.battle.updatePlayerPokemon()
    }
}