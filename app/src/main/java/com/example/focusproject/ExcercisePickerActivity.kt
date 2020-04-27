package com.example.focusproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.fragments.ArmsFragment
import kotlinx.android.synthetic.main.activity_excercise_picker.*

class ExcercisePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_picker)

        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        //TODO each muscle group is a fragment
        viewPagerAdapter.addFragment(ArmsFragment.newInstance("Hello","Hello"), "Arms")
        viewPagerAdapter.addFragment(ArmsFragment.newInstance("Hello","Hello"), "Legs")
        viewpager.setAdapter(viewPagerAdapter)
        tablayout.setupWithViewPager(viewpager)
    }
}
