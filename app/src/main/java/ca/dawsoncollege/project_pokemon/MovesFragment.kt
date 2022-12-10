package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MovesFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class MovesFragment : Fragment() {
    // TODO: Rename and change types of parameters
//    private var param1: String? = null
//    private var param2: String? = null
    private lateinit var battle: Battle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = arguments
        val battleJSON = data!!.getString("battle").toString()
        // TODO: check if wild or trainer
        this.battle = convertJSONToWildBattle(battleJSON)
//        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
//        }
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

    private fun setMoves(buttons: ArrayList<Button>){
        val moveList = this.battle.playerPokemon.moveList
        for (i in 0 until this.battle.playerPokemon.NUMBER_OF_MOVES){
            val moveButtonText = "${moveList[i].name.replace('-', ' ')}\n" +
                    "${moveList[i].PP}/${moveList[i].maxPP}\n${moveList[i].type}"
            buttons[i].text = moveButtonText

            buttons[i].setOnClickListener {
                if (moveList[i].PP > 0){
                    if (this.battle.playerPokemon.hp != 0) {
                        playTurn(moveList, buttons, i)
                        val listener = activity as MovesCallbacks
                        listener.updateUI(this.battle)
                    } else {
                        Toast.makeText(context, "${this.battle.playerPokemon.name} is fainted!", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(context, "Out of PP!", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateMovePP(button: Button, move: Move) {
        val moveButtonText = "${move.name.replace('-', ' ')}\n" +
                "${move.PP}/${move.maxPP}\n${move.type}"
        button.text = moveButtonText
    }

    // TODO: to optimize?
    private fun playTurn(moveList: ArrayList<Move>, buttons: ArrayList<Button>, i: Int){
        if (this.battle.playerPokemon.battleStat.speed >= this.battle.enemyPokemon.battleStat.speed){
            this.battle.playerMove(moveList[i])
            Log.d("MOVES_FRAG", moveList[i].toString())
            moveList[i].PP -= 1
            updateMovePP(buttons[i], moveList[i])
            this.battle.playEnemyMove()
        } else {
            this.battle.playEnemyMove()
            if (this.battle.playerPokemon.hp != 0){
                this.battle.playerMove(moveList[i])
                Log.d("MOVES_FRAG", moveList[i].toString())
                moveList[i].PP -= 1
                updateMovePP(buttons[i], moveList[i])
            } else {
                Toast.makeText(context, "${this.battle.playerPokemon.name} fainted!", Toast.LENGTH_SHORT).show()
            }
        }
        this.battle.checkPokemonFainted()
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment MovesFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            MovesFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}