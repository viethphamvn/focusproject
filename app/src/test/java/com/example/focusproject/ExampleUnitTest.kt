package com.example.focusproject


import android.content.Context
import androidx.test.platform.app.InstrumentationRegistry
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.focusproject.tools.YouTubeHelper
import kotlinx.android.synthetic.main.fragment_new_workout_edit.view.*
import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }

    @Test
    fun youtubeTest(){
        var youtubeHelper = YouTubeHelper
        var id = youtubeHelper.extractVideoIdFromUrl("https://youtu.be/Fu_oExrPX68")
        assertEquals("Fu_oExrPX68",id)
    }
}
