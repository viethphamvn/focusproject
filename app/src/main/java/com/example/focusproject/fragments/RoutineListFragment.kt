package com.example.focusproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.R
import com.example.focusproject.RoutineDetailActivity
import com.example.focusproject.adapters.FeedRecyclerViewAdapter
import com.example.focusproject.models.Routine
import com.example.focusproject.models.User
import com.example.focusproject.tools.CreateRoutine
import com.example.focusproject.tools.FireStore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val USER = "param1"

class RoutineListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var user: User? = null
    private lateinit var routineRecyclerViewAdapter : FeedRecyclerViewAdapter
    private var routineList = ArrayList<Routine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(USER) as User
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_routine_list, container, false)
        view.findViewById<RecyclerView>(R.id.routine_recycler_list_view).apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = FeedRecyclerViewAdapter(routineList){item -> doClick(item)}
            adapter = routineRecyclerViewAdapter
        }

        return view
    }

    private fun doClick(item: Routine) {
        var intent = Intent(context, RoutineDetailActivity::class.java)
        intent.putExtra("routine", item)
        startActivity(intent)
    }

    companion object {
        @JvmStatic
        fun newInstance(user: User) =
            RoutineListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(USER, user)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        fetchData()
    }

    private fun fetchData(){
        FireStore.fireStore.collection("Routines")
            .get()
            .addOnSuccessListener { result ->
                var tempArray = ArrayList<Routine>()
                routineRecyclerViewAdapter.notifyDataSetChanged()
                for (routine in result) {
                    //Check if routine is belong to followed users
                    if (routine.get("createdBy").toString() == user!!.id) {
                        tempArray.add(CreateRoutine.createRoutine(routine))
                    }
                }
                if (tempArray.size > 0) {
                    routineList = ArrayList(tempArray.sortedDescending().toList())
                    routineRecyclerViewAdapter.setNewData(routineList)
                }
            }
    }
}