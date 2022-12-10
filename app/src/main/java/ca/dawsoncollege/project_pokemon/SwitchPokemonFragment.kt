package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SwitchPokemonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SwitchPokemonFragment : Fragment() {
    private lateinit var playerTrainer: PlayerTrainer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val data = arguments
        val playerJSON = data!!.getString("player").toString()
        this.playerTrainer = convertJSONToPlayerTrainer(playerJSON)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_switch_pokemon, container, false).rootView
        val buttons = arrayListOf<Button>(
            view.findViewById(R.id.pokemon_1),
            view.findViewById(R.id.pokemon_2),
            view.findViewById(R.id.pokemon_3),
            view.findViewById(R.id.pokemon_4),
            view.findViewById(R.id.pokemon_5),
            view.findViewById(R.id.pokemon_6))
        setPokemons(buttons)
        return view
    }

    private fun setPokemons(buttons: ArrayList<Button>){
        for (i in 0 until this.playerTrainer.team.size){
            val pokemonButtonText = "${this.playerTrainer.team[i].name}\n"+
                    "LV ${this.playerTrainer.team[i].level}\n"+
                    "${this.playerTrainer.team[i].hp}/${this.playerTrainer.team[i].battleStat.maxHP}"
            buttons[i].text = pokemonButtonText
            buttons[i].visibility = View.VISIBLE
        }
    }

//    companion object {
//        /**
//         * Use this factory method to create a new instance of
//         * this fragment using the provided parameters.
//         *
//         * @param param1 Parameter 1.
//         * @param param2 Parameter 2.
//         * @return A new instance of fragment SwitchPokemonFragment.
//         */
//        // TODO: Rename and change types and number of parameters
//        @JvmStatic
//        fun newInstance(param1: String, param2: String) =
//            SwitchPokemonFragment().apply {
//                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
//                }
//            }
//    }
}