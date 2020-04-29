package com.example.focusproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import com.google.android.youtube.player.YouTubePlayerFragment

class ViewPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private var fragments : ArrayList<Fragment> = ArrayList<Fragment>()
    private var titles : ArrayList<String> = ArrayList<String>()

    override fun getItem(position: Int): Fragment {
        return fragments.get(position)
    }

    override fun getCount(): Int {
        return fragments.size
    }

    fun addFragment(fragment: Fragment, title:String){
        fragments.add(fragment)
        titles.add(title)
    }

    override fun getPageTitle(position: Int):CharSequence{
        return titles.get(position)
    }
}