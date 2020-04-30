package com.example.focusproject.fragments

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.focusproject.R
import com.example.focusproject.RoutineEditActivity
import com.example.focusproject.StartRoutineActivity
import com.example.focusproject.adapters.RoutineRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.*
import kotlinx.android.synthetic.main.fragment_routine_activity_fragment.view.mon_textview
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.properties.Delegates
import java.util.Calendar

private lateinit var routineRecyclerViewAdapter: RoutineRecyclerViewAdapter

class RoutinesListFragment : Fragment() {
    private var Exercises: ArrayList<Excercise> = ArrayList()
    private lateinit var ActiveRoutineList: ArrayList<Excercise>
    private var Routines : HashMap<String, ArrayList<Excercise>> = HashMap()
    private var selectedDate : Int = 0
    private val DAY_OF_WEEK = "DayOfWeek"
    private lateinit var currentButton : TextView
    private var buttonArray : HashMap<Int, TextView> = HashMap()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Get Arguments
        arguments?.let {
            selectedDate = it.getInt(DAY_OF_WEEK)
        }

        Exercises.add(Excercise("Push Up","pushup",30, "https://cdn.clipart.email/c679a7a0a6044e04a60a6da1d1688382_exercise-gifs-get-the-best-gif-on-giphy_480-360.gif", "",false, true))
        Exercises.add(Excercise("Pull Up","pullup",0,"", "uUKAYkQZXko", false, false))
        Exercises.add(Excercise("Rest","run",10, "https://media.giphy.com/media/3o6MbrHpaSX5Q375zW/giphy.gif", "",true, true))
        Exercises.add(Excercise("Abs","pushup",0, "", "q_LFDHqkFLo", false, false))

        Routines.put("mon", Exercises)
        Routines.put("tue", Exercises)
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

        //Create a Array of dateButton for easier control
        buttonArray.put(2, view.mon_textview)
        buttonArray.put(3, view.tue_textview)
        buttonArray.put(4, view.wed_textview)
        buttonArray.put(5, view.thu_textview)
        buttonArray.put(6, view.fri_textview)
        buttonArray.put(7, view.sat_textview)
        buttonArray.put(1, view.sun_textview)
        //Initiate RecyclerView with Adapter
        view.routine_recycler_list_view.apply {
            layoutManager = LinearLayoutManager(context)
            routineRecyclerViewAdapter = RoutineRecyclerViewAdapter(getCurrentActiveList(selectedDate))
            adapter = routineRecyclerViewAdapter
            currentButton = buttonArray.get(selectedDate)!!
            currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
        }

        //Handle Views Behavior
        view.apply {
            mon_textview.setOnClickListener {
                selectedDate = 2
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            tue_textview.setOnClickListener {
                selectedDate = 3
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            wed_textview.setOnClickListener {
                selectedDate = 4
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            thu_textview.setOnClickListener {
                selectedDate = 5
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            fri_textview.setOnClickListener {
                selectedDate = 6
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            sat_textview.setOnClickListener {
                selectedDate = 7
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            sun_textview.setOnClickListener {
                selectedDate = 1
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
                currentButton.setBackgroundColor(resources.getColor(R.color.darkgrey))
                currentButton = buttonArray.get(selectedDate)!!
                currentButton.setBackgroundColor(resources.getColor(R.color.colorPrimaryDark))
            }
            edit_routine_btn.setOnClickListener {
                startEditActivity(Routines, selectedDate)
            }
            start_workout_btn.setOnClickListener{
                var intent = Intent(context, StartRoutineActivity::class.java)
                intent.putExtra("routine", ActiveRoutineList)
                intent.putExtra("selectedDate", selectedDate)
                startActivity(intent)
            }
        }

        return view
    }

    private fun startEditActivity(Routines : HashMap<String, ArrayList<Excercise>>, dateCode: Int){
        var copyRoutine = HashMap(Routines)
        val intent = Intent(context, RoutineEditActivity::class.java)
        intent.putExtra("routine", copyRoutine)
        intent.putExtra("date", dateCode)
        startActivityForResult(intent, 100)
    }

    companion object {
        @JvmStatic
        fun newInstance(todayDate: Int) =
            RoutinesListFragment().apply {
                arguments = Bundle().apply {
                    putInt(DAY_OF_WEEK, todayDate)
                }
            }
    }

    private fun getRoutines(){
        //Do something
    }

    private fun getCurrentActiveList(selectedDate: Int): ArrayList<Excercise>{
        when(selectedDate){
            2 -> ActiveRoutineList = Routines.get("mon") as ArrayList<Excercise>
            3 -> ActiveRoutineList = Routines.get("tue") as ArrayList<Excercise>
            4 -> ActiveRoutineList = Routines.get("wed") as ArrayList<Excercise>
            5 -> ActiveRoutineList = Routines.get("thu") as ArrayList<Excercise>
            6 -> ActiveRoutineList = Routines.get("fri") as ArrayList<Excercise>
            7 -> ActiveRoutineList = Routines.get("sat") as ArrayList<Excercise>
            else -> ActiveRoutineList = Routines.get("sun") as ArrayList<Excercise>
        }
        return ActiveRoutineList
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (data != null) {
                Routines = data.getSerializableExtra("routine") as HashMap<String, ArrayList<Excercise>>
                //TODO have to update the dataset first
                routineRecyclerViewAdapter.apply {
                    updateDataSet(getCurrentActiveList(selectedDate))
                    notifyDataSetChanged()
                }
            }
        }
    }
}
