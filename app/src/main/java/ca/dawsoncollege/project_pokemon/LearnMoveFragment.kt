package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class LearnMoveFragment : Fragment() {
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
            view.findViewById(R.id.learn_1),
            view.findViewById(R.id.learn_2),
            view.findViewById(R.id.learn_3),
            view.findViewById(R.id.learn_4))
        setMoves(buttons)
        view.findViewById<Button>(R.id.cancel_learn).setOnClickListener {
            val listener = activity as Callbacks
            listener.learnMove(9)
        }
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
                listener.learnMove(i)
            }
        }
    }
}