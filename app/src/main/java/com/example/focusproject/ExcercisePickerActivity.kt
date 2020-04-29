package com.example.focusproject

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.widget.Toolbar
import androidx.viewpager.widget.ViewPager
import com.example.focusproject.adapters.ViewPagerAdapter
import com.example.focusproject.fragments.ArmsFragment
import com.example.focusproject.models.Excercise
import kotlinx.android.synthetic.main.activity_excercise_picker.*

class ExcercisePickerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_excercise_picker)

        var viewPagerAdapter = ViewPagerAdapter(supportFragmentManager)
        //TODO each muscle group is a fragment
        viewPagerAdapter.addFragment(ArmsFragment.newInstance("Hello","Hello"), "Arms")
        viewPagerAdapter.addFragment(ArmsFragment.newInstance("Hello","Hello"), "Legs")
        viewpager.adapter = viewPagerAdapter
        tablayout.setupWithViewPager(viewpager)
    }

    fun itemClick(item: Excercise){
        var returnIntent = Intent()
        returnIntent.putExtra("excercise", item)
        setResult(Activity.RESULT_OK, returnIntent)
        finish()
    }
}
