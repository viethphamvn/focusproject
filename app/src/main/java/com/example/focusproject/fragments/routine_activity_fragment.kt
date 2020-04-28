package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusproject.R
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.mon_textview

private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

class routine_activity_fragment : Fragment() {
    private var Exercises: ArrayList<Excercise> = ArrayList()
    private var Exercises2: ArrayList<Excercise> = ArrayList()
    private var Routines : HashMap<String, List<Excercise>> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Exercises.add(Excercise("Push Up","pushup",30, "addr1", true))
        Exercises.add(Excercise("Pull Up","pullup",0,"addr2", false))
        Exercises.add(Excercise("Run","run",10, "addr3", true))
        Exercises.add(Excercise("Run","pushup",20, "addr1", true))
        Exercises.add(Excercise("Chest Press","pullup",0,"addr2", false))
        Exercises.add(Excercise("Run","run",10, "addr3", true))

        Exercises2.add(Excercise("Run","pushup",20, "addr1", true))
        Exercises2.add(Excercise("Chest Press","pullup",0,"addr2", false))
        Exercises2.add(Excercise("Run","run",10, "addr3", true))

        Routines.put("mon", Exercises)
        Routines.put("tue", Exercises2)
        Routines.put("wed", Exercises)
        Routines.put("thu", Exercises)
        Routines.put("fri", Exercises)
        Routines.put("sat", Exercises)
        Routines.put("sun", Exercises)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_routine_activity_fragment, container, false)

        //Initiate RecyclerView with Adapter
        view.routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(Exercises)
            adapter = routineRecyclerViewAdapter
        }

        //Handle Views Behavior
        view.apply {
            mon_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("mon") as ArrayList<Excercise>)
            }
            tue_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("tue") as ArrayList<Excercise>)
            }
            wed_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("wed") as ArrayList<Excercise>)
            }
            thu_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("thu") as ArrayList<Excercise>)
            }
            fri_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("fri") as ArrayList<Excercise>)
            }
            sat_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("sat") as ArrayList<Excercise>)
            }
            sun_textview.setOnClickListener {
                routineRecyclerViewAdapter.updateDataSet(Routines.get("sun") as ArrayList<Excercise>)
            }
        }


        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            routine_activity_fragment()
    }

    private fun getRoutines(){
        //Do something
    }


}
