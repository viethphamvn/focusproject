package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.focusproject.R
import kotlinx.android.synthetic.main.fragment_excercise_progress.view.*
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val CURRENT_SET = "param1"
private const val TOTAL_SET = "param2"
private const val TOTAL_REP = "param3"


class ExcerciseProgressFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var curSet: Int? = null
    private var totSet: Int? = null
    private var totRep: Int? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            curSet = it.getInt(CURRENT_SET)
            totRep = it.getInt(TOTAL_REP)
            totSet = it.getInt(TOTAL_SET)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_excercise_progress, container, false)
        view.setTextView.text = String.format("%d/%d",curSet,totSet)
        view.repTextView.text = String.format("%d",totRep)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(curSet: Int, totSet: Int, totRep: Int) =
            ExcerciseProgressFragment().apply {
                arguments = Bundle().apply {
                    putInt(CURRENT_SET, curSet)
                    putInt(TOTAL_SET, totSet)
                    putInt(TOTAL_REP,totRep)
                }
            }
    }
}
