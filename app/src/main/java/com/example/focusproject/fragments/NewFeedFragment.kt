package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.focusproject.R
import com.example.focusproject.adapters.FeedRecyclerViewAdapter
import com.example.focusproject.models.Routine
import com.example.focusproject.tools.CreateRoutine
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_new_feed.view.*
import java.util.*
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.collections.ArrayList

class NewFeedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var fragmentView: View
    private var routineList: ArrayList<Routine> = ArrayList()
    lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter
    var tempArray = ArrayList<Routine>()
    lateinit var swipeRefreshLayout: SwipeRefreshLayout

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentView =  inflater.inflate(R.layout.fragment_new_feed, container, false)

        swipeRefreshLayout = fragmentView.swipeToRefreshLayout as SwipeRefreshLayout
        swipeRefreshLayout.setOnRefreshListener(this)

        fragmentView.feedRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            feedRecyclerViewAdapter = FeedRecyclerViewAdapter(routineList)
            adapter = feedRecyclerViewAdapter
        }
        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData(){
        FirebaseFirestore.getInstance().collection("Routines")
            .get()
            .addOnSuccessListener { result ->
                routineList.clear()
                tempArray.clear()
                feedRecyclerViewAdapter.notifyDataSetChanged()
                for (routine in result) {
                    //Check if routine is belong to followed users
                    tempArray.add(CreateRoutine.createRoutine(routine))
                }
                if (tempArray.size > 0) {
                    routineList = ArrayList(tempArray.sortedDescending().toList())
                    feedRecyclerViewAdapter.setNewData(routineList)
                }
            }
    }

    companion object {
        fun newInstance() =
            NewFeedFragment()
    }

    override fun onRefresh() {
        fetchData()
        swipeRefreshLayout.isRefreshing = false
    }
}
