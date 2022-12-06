package ca.dawsoncollege.project_pokemon

import android.util.Log
import android.view.DragEvent
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class DragListener internal constructor(private val listener: CustomListener) :
    View.OnDragListener {
    private var isDropped = false
    override fun onDrag(v: View, event: DragEvent): Boolean {
        when (event.action) {
            DragEvent.ACTION_DROP -> {
                isDropped = true
                var positionTarget = -1
                val viewSource = event.localState as View?
                val viewId = v.id
                val frameLayoutItem = R.id.frame_layout_item
                val emptyTextView1 = R.id.empty_list_text_view_1
                val emptyTextView2 = R.id.empty_list_text_view_2
                val recyclerView1 = R.id.recycler_view_1
                val recyclerView2 = R.id.recycler_view_2
                when (viewId) {
                    frameLayoutItem, emptyTextView1, emptyTextView2, recyclerView1, recyclerView2 -> {
                        val target: RecyclerView
                        val adapterTarget: CustomAdapter?
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
                        adapterTarget = target.adapter as CustomAdapter?
                        if (viewSource != null) {
                            val source = viewSource.parent as RecyclerView
                            val adapterSource = source.adapter as CustomAdapter?
                            val positionSource = viewSource.tag as Int
                            val list: Pokemon? = adapterSource?.getList()?.get(positionSource)
                            val listSource = adapterSource?.getList()?.apply {
                                if (target.id == recyclerView1 && adapterTarget!!.itemCount == 6) {
                                    Log.d(
                                        "ListCheck",
                                        "Cannot have more than 6 items in recyclerView1"
                                    )
                                    return false
                                } else if (source.id == recyclerView1 && this.size == 1) {
                                    Log.d(
                                        "ListCheck",
                                        "recyclerView1 cannot be empty"
                                    )
                                    return false
                                } else {
                                    removeAt(positionSource)
                                }
                            }
                            listSource?.let { adapterSource.updateList(it) }
                            adapterSource?.notifyDataSetChanged()
                            Log.d("ListCheck", "Source: " + listSource.toString())
                            val customListTarget = adapterTarget?.getList()
                            if (positionTarget >= 0) {
                                list?.let { customListTarget?.add(positionTarget, it) }
                            } else {
                                list?.let { customListTarget?.add(it) }
                            }
                            customListTarget?.let { adapterTarget.updateList(it) }
                            adapterTarget?.notifyDataSetChanged()
                            Log.d("ListCheck", "Target: " + customListTarget.toString())
                            if (source.id == recyclerView2 && adapterSource?.itemCount ?: 0 < 1) {
                                listener.setEmptyList(View.VISIBLE, recyclerView2, emptyTextView2)
                            }
                            if (viewId == emptyTextView2) {
                                listener.setEmptyList(View.GONE, recyclerView2, emptyTextView2)
                            }
                            if (source.id == recyclerView1 && adapterSource?.itemCount ?: 0 < 1) {
                                listener.setEmptyList(View.VISIBLE, recyclerView1, emptyTextView1)
                            }
                            if (viewId == emptyTextView1) {
                                listener.setEmptyList(View.GONE, recyclerView1, emptyTextView1)
                            }
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
}