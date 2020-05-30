package com.example.focusproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.focusproject.fragments.MuscleGroupFragment
import com.example.focusproject.fragments.NewWorkoutEditFragment

class ViewPagerAdapter(var numPage: Int, var fragments: ArrayList<Fragment>, fm: FragmentActivity) : FragmentStateAdapter(fm) {
    override fun getItemCount(): Int {
        return numPage
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }
}