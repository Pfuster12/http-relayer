package com.yabu.httprelayerexample.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yabu.httprelayer.*
import com.yabu.httprelayerexample.R
import com.yabu.httprelayerexample.data.HttpService

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        getSharedPreferences("test", Context.MODE_PRIVATE)
            .edit()
            .putBoolean(HTTP_RELAYER_PREFERENCE_KEY, true)
            .apply()

        performNetworkCall()
    }

    private fun performNetworkCall() {
        val relayer = HttpRelayer.with(this)?.create()
        HttpService.getTestUrl(this, relayer)
    }
}
