package com.example.focusproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.focusproject.fragments.MuscleGroupFragment
import com.example.focusproject.fragments.NewWorkoutEditFragment

class ViewPagerAdapter(var numPage: Int, fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return numPage
    }

    override fun createFragment(position: Int): Fragment {
        return when (position){
            0 -> NewWorkoutEditFragment.newInstance()
            1 -> MuscleGroupFragment.newInstance("", true)
            2 -> MuscleGroupFragment.newInstance("Abs", false)
            else -> MuscleGroupFragment.newInstance("Arms", false)
        }
    }
}