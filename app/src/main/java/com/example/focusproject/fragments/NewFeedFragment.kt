package com.example.focusproject.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

import com.example.focusproject.R
import com.example.focusproject.RoutineDetailActivity
import com.example.focusproject.adapters.ExerciseRecyclerViewAdapter
import com.example.focusproject.adapters.FeedRecyclerViewAdapter
import com.example.focusproject.models.Exercise
import com.example.focusproject.models.Routine
import com.example.focusproject.tools.CreateExercise
import com.example.focusproject.tools.CreateRoutine
import com.example.focusproject.tools.CreateUser
import com.example.focusproject.tools.FireStore
import kotlinx.android.synthetic.main.fragment_new_feed.view.*
import kotlin.collections.ArrayList

class NewFeedFragment : Fragment(), SwipeRefreshLayout.OnRefreshListener {
    private lateinit var fragmentView: View
    private var routineList: ArrayList<Routine> = ArrayList()
    private lateinit var feedRecyclerViewAdapter: FeedRecyclerViewAdapter
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
            feedRecyclerViewAdapter = FeedRecyclerViewAdapter(routineList){item -> doClick(item)}
            adapter = feedRecyclerViewAdapter
        }
        return fragmentView
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun doClick(item: Routine) {
        var intent = Intent(context, RoutineDetailActivity::class.java)
        intent.putExtra("routine", item)
        startActivity(intent)
    }

    private fun fetchData(){
        FireStore.fireStore.collection("Users").document(FireStore.currentUser!!.uid)
            .get()
            .addOnSuccessListener {
                var currentUser = CreateUser.createUser(it)
                if (currentUser.following.size > 0){
                    FireStore.fireStore.collection("Routines")
                        .get()
                        .addOnSuccessListener { result ->
                            routineList.clear()
                            tempArray.clear()
                            for (routine in result) {
                                //Check if routine is belong to followed users
                                if (currentUser.following.contains(routine.get("createdBy").toString())) {
                                    tempArray.add(CreateRoutine.createRoutine(routine))
                                }
                            }
                            if (tempArray.size > 0) {
                                routineList = ArrayList(tempArray.sortedDescending().toList())
                                feedRecyclerViewAdapter.setNewData(routineList)
                            }
                        }
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
