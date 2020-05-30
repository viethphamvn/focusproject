package com.example.focusproject.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

import com.example.focusproject.R
import com.example.focusproject.adapters.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_excercise_picker.view.*

class ExercisePickerFragment : Fragment() {
    private val tabTitle = arrayOf("New Workout", "You Created", "Abs", "Arms")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_excercise_picker, container, false)

        var fragments = ArrayList<Fragment>()
        fragments.add(NewWorkoutEditFragment.newInstance())
        fragments.add(MuscleGroupFragment.newInstance("", true))
        fragments.add(MuscleGroupFragment.newInstance("Abs", false))
        fragments.add(MuscleGroupFragment.newInstance("Arms", false))

        view.viewpager.adapter = activity?.let { ViewPagerAdapter(4, fragments, it) }

        var tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, view.viewpager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = ExercisePickerFragment()
    }
}
