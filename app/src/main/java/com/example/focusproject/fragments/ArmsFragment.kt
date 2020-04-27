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

        Exercises.add(Excercise("Push Up","pushup","addr1"))
        Exercises.add(Excercise("Pull Up","pullup","addr2"))
        Exercises.add(Excercise("Run","run","addr3"))
        Exercises.add(Excercise("Push Up","pushup","addr1"))
        Exercises.add(Excercise("Pull Up","pullup","addr2"))
        Exercises.add(Excercise("Run","run","addr3"))
        Exercises.add(Excercise("Push Up","pushup","addr1"))
        Exercises.add(Excercise("Pull Up","pullup","addr2"))
        Exercises.add(Excercise("Run","run","addr3"))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_arms, container, false)
        view.excerciseItemRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2)
            excerciseRecyclerViewAdapter = ExcerciseRecyclerViewAdapter(Exercises)
            adapter = excerciseRecyclerViewAdapter
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
