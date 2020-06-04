package com.example.focusproject


import com.example.focusproject.tools.YouTubeHelper
import org.junit.Assert.assertEquals
import org.junit.Test

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
        val youtubeHelper = YouTubeHelper
        val id = youtubeHelper.extractVideoIdFromUrl("https://youtu.be/Fu_oExrPX68")
        assertEquals("Fu_oExrPX68",id)
    }
}
