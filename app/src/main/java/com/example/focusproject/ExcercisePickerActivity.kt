package com.example.focusproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.focusproject.adapters.ViewPagerAdapter
import kotlinx.android.synthetic.main.activity_excercise_picker.*

class ExcercisePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_picker)

        setSupportActionBar(toolbarWithSearchBar)

        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        //TODO each muscle group is a fragment
        /**viewPagerAdapter.addFragment()
         * viewpager.setAdapter(viewPagerAdapter)
         * tabLayout.setupWithViewPager(viewpager)
        **/
    }
}
