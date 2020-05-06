package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.focusproject.R
import kotlinx.android.synthetic.main.fragment_excercise_progress.view.*
import java.util.*

private const val ORM = "param1"
private const val TOTAL_REP = "param2"


class ExcerciseProgressFragment : Fragment() {
    private var orm: Long? = null
    private var totRep: Long? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            orm = it.getLong(ORM)
            totRep = it.getLong(TOTAL_REP)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view =  inflater.inflate(R.layout.fragment_excercise_progress, container, false)
        view.ormTextView.text = String.format("%d",orm)
        view.repTextView.text = String.format("%d",totRep)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(orm: Long, totRep: Long) =
            ExcerciseProgressFragment().apply {
                arguments = Bundle().apply {
                    putLong(ORM, orm)
                    putLong(TOTAL_REP,totRep)
                }
            }
    }
}
