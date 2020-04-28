package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusproject.R
import com.example.focusproject.adapters.ExcerciseRecyclerViewAdapter
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.fragment_arms.view.*
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

/**
 * A simple [Fragment] subclass.
 * Use the [routine_activity_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class routine_activity_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private var Exercises: ArrayList<Excercise> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Exercises.add(Excercise("Push Up","pushup",30, "addr1", true))
        Exercises.add(Excercise("Pull Up","pullup",0,"addr2", false))
        Exercises.add(Excercise("Run","run",10, "addr3", true))
        Exercises.add(Excercise("Run","pushup",20, "addr1", true))
        Exercises.add(Excercise("Chest Press","pullup",0,"addr2", false))
        Exercises.add(Excercise("Run","run",10, "addr3", true))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_routine_activity_fragment, container, false)
        view.routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(Exercises)
            adapter = routineRecyclerViewAdapter
        }
        return view
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment routine_activity_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            routine_activity_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
