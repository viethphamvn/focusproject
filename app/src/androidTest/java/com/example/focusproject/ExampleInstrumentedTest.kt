package com.example.focusproject

import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.focusproject", appContext.packageName)
    }

    @Test
    fun getDuration(){
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val API_KEY = "AIzaSyDGVmEwQlfqzbybEhpkyXTfI2L0uSlU1-s"
        val vidId = "9bZkp7q19f0"
        val url = "https://www.googleapis.com/youtube/v3/videos?id=$vidId&part=contentDetails&key=$API_KEY"
        // Instantiate the RequestQueue.
        val queue = Volley.newRequestQueue(appContext)

        // Request a string response from the provided URL.
        val stringRequest = StringRequest(
            Request.Method.GET, url,
            Response.Listener { response ->
                val resultJson = JSONObject(response)
                assertEquals("PT4M13S",resultJson.get("duration"))
            },
            Response.ErrorListener {})

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }
}
