package com.example.focusproject.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.mon_textview
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

class routine_activity_fragment : Fragment() {
    private var Exercises: ArrayList<Excercise> = ArrayList()
    private var Exercises2: ArrayList<Excercise> = ArrayList()
    private lateinit var ActiveRoutineList: ArrayList<Excercise>
    private var Routines : HashMap<String, List<Excercise>> = HashMap()
    private lateinit var touchHelper : ItemTouchHelper

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

        //Handle Drag and Drop Item
        touchHelper = ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val sourcePosition = viewHolder.adapterPosition
                val targetPosition = target.adapterPosition
                Collections.swap(ActiveRoutineList, sourcePosition, targetPosition)
                routineRecyclerViewAdapter.notifyItemMoved(sourcePosition, targetPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                TODO("Not yet implemented")
            }

        })
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
            //TODO Pass routine list associate with current day. Set ActiveExerciseList to the list.
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(Exercises)
            adapter = routineRecyclerViewAdapter
        }
        touchHelper.attachToRecyclerView(view.routine_recycler_list_view)

        //Handle Views Behavior
        view.apply {
            mon_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("mon") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("mon") as ArrayList<Excercise>
            }
            tue_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("tue") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("tue") as ArrayList<Excercise>
            }
            wed_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("wed") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("wed") as ArrayList<Excercise>
            }
            thu_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("thu") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("thu") as ArrayList<Excercise>
            }
            fri_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("fri") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("fri") as ArrayList<Excercise>
            }
            sat_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("sat") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("sat") as ArrayList<Excercise>
            }
            sun_textview.setOnClickListener {
                routineRecyclerViewAdapter.apply {
                    updateDataSet(Routines.get("sun") as ArrayList<Excercise>)
                    notifyDataSetChanged()
                }
                ActiveRoutineList = Routines.get("sun") as ArrayList<Excercise>
            }
            edit_routine_btn.setOnClickListener {
                startActivity(Intent(context, RoutineEditActivity::class.java))
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
