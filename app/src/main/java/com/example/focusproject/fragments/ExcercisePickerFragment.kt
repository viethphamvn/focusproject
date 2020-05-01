package com.example.focusproject.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.focusproject.CreateExcerciseActivity
import com.example.focusproject.ExcercisePickerActivity

import com.example.focusproject.R
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.activity_excercise_picker.*
import kotlinx.android.synthetic.main.fragment_excercise_picker.view.*

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ExcercisePickerFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_excercise_picker, container, false)
        var viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(ArmsFragment.newInstance("Hello","Hello"), "Arms")
        viewPagerAdapter.addFragment(ArmsFragment.newInstance("Hello","Hello"), "Legs")
        view.viewpager.adapter = viewPagerAdapter
        view.tablayout.setupWithViewPager(viewpager)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ExcercisePickerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
