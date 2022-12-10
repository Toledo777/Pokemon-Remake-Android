package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

/**
 * A simple [Fragment] subclass.
 * Use the [SwitchPokemonFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SwitchPokemonFragment : Fragment() {
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

    // set text and listener to button corresponding to pokemon in team
    private fun setPokemons(buttons: ArrayList<Button>){
        for (i in 0 until this.battle.playerTrainer.team.size){
            val pokemonButtonText = "${this.battle.playerTrainer.team[i].name}\n"+
                    "LV ${this.battle.playerTrainer.team[i].level}\n"+
                    "${this.battle.playerTrainer.team[i].hp}/${this.battle.playerTrainer.team[i].battleStat.maxHP}"
            buttons[i].text = pokemonButtonText
//            buttons[i].visibility = View.VISIBLE
            if (i == 0)
                buttons[i].setOnClickListener {
                    Toast.makeText(context, "${this.battle.playerPokemon.name} is already in battle!", Toast.LENGTH_SHORT).show()
                }
            else{
                buttons[i].setOnClickListener {
                    // switch first pokemon with ith pokemon
                    val switchTo = this.battle.playerTrainer.team[i]
                    this.battle.playerTrainer.team[i] = this.battle.playerTrainer.team[0]
                    this.battle.playerTrainer.team[0] = switchTo
                    this.battle.playerPokemon = switchTo
                    val listener = activity as Callbacks
                    listener.updatePokemonUI(this.battle)
                    replaceWithMovesFragment()
                }
            }
        }
    }

    private fun replaceWithMovesFragment(){
        val battleActivity = activity as BattleActivity
        val movesFragment = MovesFragment()
        val bundle = Bundle()
        bundle.putString("battle", convertBattleToJSON(this.battle))
        movesFragment.arguments = bundle
        battleActivity.supportFragmentManager.beginTransaction().apply {
            replace(R.id.battle_menu_fragment, movesFragment)
            commit()
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