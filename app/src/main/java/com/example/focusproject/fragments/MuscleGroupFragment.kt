package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.focusproject.CreateExcerciseActivity
import com.example.focusproject.ExcercisePickerActivity

import com.example.focusproject.R
import com.example.focusproject.adapters.ExcerciseRecyclerViewAdapter
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.fragment_arms.view.excerciseItemRecyclerView

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ArmsFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var excerciseRecyclerViewAdapter: ExcerciseRecyclerViewAdapter
    private var Exercises: ArrayList<Excercise> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        Exercises.add(Excercise("Push Up","pushup",30, "https://cdn.clipart.email/c679a7a0a6044e04a60a6da1d1688382_exercise-gifs-get-the-best-gif-on-giphy_480-360.gif", "",false, true, 3))
        Exercises.add(Excercise("Pull Up","pullup",0,"", "uUKAYkQZXko", false, false, 0))
        Exercises.add(Excercise("Rest","run",10, "https://media.giphy.com/media/3o6MbrHpaSX5Q375zW/giphy.gif", "",true, true, 0))
        Exercises.add(Excercise("Abs","pushup",0, "", "q_LFDHqkFLo", false, false, 2))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_arms, container, false)
        view.excerciseItemRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            excerciseRecyclerViewAdapter = ExcerciseRecyclerViewAdapter(Exercises) { item -> doClick(item)}
            adapter = excerciseRecyclerViewAdapter
        }
        return view
    }

    fun doClick(item: Excercise){
        if (activity is CreateExcerciseActivity){
            (activity as CreateExcerciseActivity).onItemClick(item)
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ArmsFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ArmsFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
