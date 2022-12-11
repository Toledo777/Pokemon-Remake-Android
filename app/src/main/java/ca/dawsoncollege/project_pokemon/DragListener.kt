package ca.dawsoncollege.project_pokemon

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.*

class DragListener internal constructor(
    private val listener: CustomListener,
    private val userDao: UserDao,
    private val context: Context
) :
    View.OnDragListener {
    private var isDropped = false
    private var positionTarget = -1
    private val frameLayoutItem = R.id.frame_layout_item
    private val emptyTextView1 = R.id.empty_list_text_view_1
    private val emptyTextView2 = R.id.empty_list_text_view_2
    private val recyclerView1 = R.id.recycler_view_1
    private val recyclerView2 = R.id.recycler_view_2

    @SuppressLint("NotifyDataSetChanged")
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                isDropped = true
                val viewSource = event.localState as View?
                when (val viewId = v.id) {
                    frameLayoutItem, emptyTextView1, emptyTextView2, recyclerView1, recyclerView2 -> {
                        val target: RecyclerView
                        when (viewId) {
                            emptyTextView1, recyclerView1 -> target =
                                v.rootView.findViewById<View>(recyclerView1) as RecyclerView
                            emptyTextView2, recyclerView2 -> target =
                                v.rootView.findViewById<View>(recyclerView2) as RecyclerView
                            else -> {
                                target = v.parent as RecyclerView
                                positionTarget = v.tag as Int
                            }
                        }
                        val adapterTarget: CustomAdapter? = target.adapter as CustomAdapter?
                        if (viewSource != null) {
                            val source = viewSource.parent as RecyclerView
                            val adapterSource = source.adapter as CustomAdapter?
                            val positionSource = viewSource.tag as Int
                            val pokemon: Pokemon? = adapterSource?.getList()?.get(positionSource)
                            val listSource = adapterSource?.getList()

                            if (!movePokemonFromSource(
                                    listSource as ArrayList<Pokemon>,
                                    source.id,
                                    target.id,
                                    positionSource,
                                    adapterTarget!!,
                                    adapterSource
                                )
                            ) return false

                            val pokemonListTarget = movePokemonToTarget(adapterTarget, pokemon!!)

                            runBlocking {
                                updateDatabase(
                                    source.id,
                                    target.id,
                                    listSource,
                                    pokemonListTarget
                                )
                            }
                            Log.d(
                                "ListCheck",
                                "Target: " + pokemonListTarget.map { it.name }.toString()
                            )
                            updateUI(source.id, adapterSource, viewId)
                        }
                    }
                }
            }
        }
        if (!isDropped && event.localState != null) {
            (event.localState as View).visibility = View.VISIBLE
        }
        return true
    }

    // Add pokemon to the target recycler view
    @SuppressLint("NotifyDataSetChanged")
    private fun movePokemonToTarget(
        targetAdapter: CustomAdapter,
        pokemon: Pokemon
    ): ArrayList<Pokemon> {
        val pokemonListTarget = targetAdapter.getList()
        if (positionTarget >= 0) {
            pokemon.let { pokemonListTarget.add(positionTarget, it) }
        } else {
            pokemon.let { pokemonListTarget.add(it) }
        }
        pokemonListTarget.let { targetAdapter.updateList(it) }
        targetAdapter.notifyDataSetChanged()
        return pokemonListTarget as ArrayList<Pokemon>
    }

    // Move the pokemon from the source recycler view
    @SuppressLint("NotifyDataSetChanged")
    private fun movePokemonFromSource(
        sourceList: ArrayList<Pokemon>,
        sourceId: Int,
        targetId: Int,
        sourcePosition: Int,
        targetAdapter: CustomAdapter,
        sourceAdapter: CustomAdapter
    ): Boolean {
        if (sourceId == recyclerView2 && targetId == recyclerView1 && targetAdapter.itemCount == 6) {
            Toast.makeText(
                context,
                "You can only have 6 Pokemons in your team",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else if (sourceId == recyclerView1 && sourceList.size == 1) {
            Toast.makeText(
                context,
                "Your Pokemon team cannot be empty",
                Toast.LENGTH_SHORT
            ).show()
            return false
        } else {
            sourceList.removeAt(sourcePosition)
        }
        sourceList.let { sourceAdapter.updateList(it) }
        sourceAdapter.notifyDataSetChanged()
        Log.d("ListCheck", "Source: " + sourceList.map { it.name }.toString())
        return true
    }

    // Update pokemon team and collection in the database
    private suspend fun updateDatabase(
        sourceId: Int,
        targetId: Int,
        sourceList: ArrayList<Pokemon>,
        targetList: ArrayList<Pokemon>
    ) {
        withContext(Dispatchers.IO) {
            when (sourceId) {
                recyclerView1 -> userDao.updateTeam(sourceList)
                recyclerView2 -> userDao.updateCollection(sourceList)
            }
            when (targetId) {
                recyclerView1 -> userDao.updateTeam(targetList)
                recyclerView2 -> userDao.updateCollection(targetList)
            }
        }
    }

    private fun updateUI(sourceId: Int, sourceAdapter: CustomAdapter, viewId: Int) {
        if (sourceId == recyclerView2 && sourceAdapter.itemCount < 1) {
            listener.setEmptyList(View.VISIBLE, recyclerView2, emptyTextView2)
        }
        if (viewId == emptyTextView2) {
            listener.setEmptyList(View.GONE, recyclerView2, emptyTextView2)
        }
        if (sourceId == recyclerView1 && sourceAdapter.itemCount < 1) {
            listener.setEmptyList(View.VISIBLE, recyclerView1, emptyTextView1)
        }
        if (viewId == emptyTextView1) {
            listener.setEmptyList(View.GONE, recyclerView1, emptyTextView1)
        }
    }
}