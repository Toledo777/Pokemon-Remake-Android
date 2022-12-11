package ca.dawsoncollege.project_pokemon

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import ca.dawsoncollege.project_pokemon.databinding.FragmentChangeTeamBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class ChangeTeamFragment : Fragment(R.layout.fragment_change_team), CustomListener {

    private var _binding: FragmentChangeTeamBinding? = null
    private val binding get() = _binding!!
    private lateinit var userDao: UserDao

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "Trainer-Database"
        ).build()

        this.userDao = db.userDao()

        _binding = FragmentChangeTeamBinding.inflate(inflater, container, false)
        runBlocking {
            withContext(Dispatchers.IO) {
                this@ChangeTeamFragment.userDao.savePlayerTrainer(PlayerTrainer("Paul"))
            }
            launch(Dispatchers.IO) {

                // ADD TEST DATA (TO BE REMOVED)
                if (this@ChangeTeamFragment.userDao.fetchPlayerSave().team.isEmpty()) {
                    this@ChangeTeamFragment.userDao.updateTeam(
                        arrayListOf(
                            Pokemon(5, "charmander"),
                            Pokemon(5, "pikachu"),
                            Pokemon(5, "rapidash"),
                            Pokemon(5, "zapdos")
                        )
                    )
                }
                _binding!!.recyclerView1.init(
                    this@ChangeTeamFragment.userDao.fetchPlayerSave().team,
                    binding.emptyListTextView1,
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                )
            }
            launch(Dispatchers.IO) {

                _binding!!.recyclerView2.init(
                    this@ChangeTeamFragment.userDao.fetchPlayerSave().pokemonCollection,
                    binding.emptyListTextView2,
                    GridLayoutManager(context, 6)
                )
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun RecyclerView.init(
        list: List<Pokemon>,
        emptyTextView: TextView,
        layoutManager: RecyclerView.LayoutManager
    ) {
        this.layoutManager = layoutManager
        val adapter = CustomAdapter(list, this@ChangeTeamFragment, userDao, context)
        this.adapter = adapter
        emptyTextView.setOnDragListener(adapter.dragInstance)
        this.setOnDragListener(adapter.dragInstance)
        if (list.isEmpty()) {
            emptyTextView.visibility = View.VISIBLE;
        }
    }

    override fun setEmptyList(visibility: Int, recyclerView: Int, emptyTextView: Int) {
        binding.root.getViewById(recyclerView).visibility = View.VISIBLE
        binding.root.getViewById(emptyTextView).visibility = visibility
    }
}