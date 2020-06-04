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
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore

private const val USER = "param1"
private const val DOCUMENT = "param2"

class RoutineListFragment : Fragment() {
    private var user: User? = null
    private var documentPath: String? = null
    private lateinit var routineRecyclerViewAdapter : FeedRecyclerViewAdapter
    private var routineList = ArrayList<Routine>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            user = it.getSerializable(USER) as User
            documentPath = it.getString(DOCUMENT)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_routine_list, container, false)
        view.findViewById<RecyclerView>(R.id.routine_recycler_list_view).apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = FeedRecyclerViewAdapter(routineList){item -> doClick(item)}
            adapter = routineRecyclerViewAdapter
        }

        return view
    }

    private fun doClick(item: Routine) {
        val intent = Intent(context, RoutineDetailActivity::class.java)
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

        fun newInstance(user: User, documentPath: String) =
            RoutineListFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(USER, user)
                    putString(DOCUMENT, documentPath)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        routineList.clear()
        fetchData()
    }

    private fun fetchData(){
        if (documentPath == null){
            FirebaseFirestore.getInstance().collection("Routines")
                .get()
                .addOnSuccessListener { result ->
                    //Check if routine is belong to followed users
                    for (routine in result){
                        if (routine.get("createdBy").toString() == user!!.id){
                            createRoutines(routine)
                        }
                    }
                }
        } else {
            FirebaseFirestore.getInstance().collection("Users").document(user!!.id)
                .get()
                .addOnSuccessListener { result ->
                    if (result.get("savedRoutines") != null) {
                        val savedRoutines =
                            result.get("savedRoutines") as ArrayList<String>//this is a list of routine id
                        for (id in savedRoutines) {
                            FirebaseFirestore.getInstance().collection("Routines").document(id)
                                .get()
                                .addOnSuccessListener { routine ->
                                    createRoutines(routine)
                                }
                        }
                    }
                }
        }
    }

    private fun createRoutines(routine: DocumentSnapshot){
        routineList.add(CreateRoutine.createRoutine(routine))
        if (routineList.size > 0) {
            routineList = ArrayList(routineList.sortedDescending().toList())
            routineRecyclerViewAdapter.setNewData(routineList)
        }
    }

}