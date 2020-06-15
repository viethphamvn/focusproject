package com.example.focusproject.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.focusproject.R
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.models.User
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.android.synthetic.main.fragment_excercise_picker.view.*

class RoutineAndExercisePickerFragment : Fragment() {
    private val tabTitle = arrayOf("New Exercise", "Created Exercises", "Created Routines", "Saved Routines")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_routine_and_exercise_picker, container, false)

        val fragments = ArrayList<Fragment>()
        fragments.add(NewWorkoutEditFragment.newInstance())
        fragments.add(MuscleGroupFragment.newInstance("", true))
        fragments.add(RoutineListFragment.newInstance(User.currentUser))
        fragments.add(RoutineListFragment.newInstance(User.currentUser, "savedRoutines"))

        view.viewpager.adapter = activity?.let { ViewPagerAdapter(4, fragments, it) }

        val tabLayout = view.findViewById<TabLayout>(R.id.tab_layout)
        TabLayoutMediator(tabLayout, view.viewpager) { tab, position ->
            tab.text = tabTitle[position]
        }.attach()
        return view
    }

    companion object {
        @JvmStatic
        fun newInstance() = RoutineAndExercisePickerFragment()
    }
}