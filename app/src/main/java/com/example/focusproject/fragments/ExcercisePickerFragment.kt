package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.example.focusproject.R
import com.example.focusproject.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_excercise_picker.view.*

class ExcercisePickerFragment : Fragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_excercise_picker, container, false)
        var viewPagerAdapter = ViewPagerAdapter(childFragmentManager)
        viewPagerAdapter.addFragment(NewWorkoutEditFragment.newInstance(),"New")
        viewPagerAdapter.addFragment(MuscleGroupFragment.newInstance("Abs"), "Abs")
        view.viewpager.adapter = viewPagerAdapter
        view.tablayout.setupWithViewPager(view.viewpager)
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExcercisePickerFragment()
    }
}
