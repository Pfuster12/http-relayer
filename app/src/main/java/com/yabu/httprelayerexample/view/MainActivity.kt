package com.yabu.httprelayerexample.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.yabu.httprelayer.*
import com.yabu.httprelayerexample.R
import com.yabu.httprelayerexample.data.HttpService
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    companion object {
        /**
         * A Shared Preference Key to check if [HttpRelayer] should generate a dialog.
         */
        const val HTTP_RELAYER_PREFERENCE_KEY = "httprelayer.HTTP_RELAYER_PREFERENCE_KEY"
    }

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
        val relayer = HttpRelayer.with(this,
            getSharedPreferences("test", Context.MODE_PRIVATE)
                .getBoolean(HTTP_RELAYER_PREFERENCE_KEY, false))
            ?.create()
        HttpService.getTestUrl(this, relayer) { str ->
            test_string.text = str
        }
    }

    private fun performErrorNetworkCall() {
        val relayer = HttpRelayer.with(this,
            getSharedPreferences("test", Context.MODE_PRIVATE)
                .getBoolean(HTTP_RELAYER_PREFERENCE_KEY, false))
            ?.create()
        relayer?.setVerbose()
        HttpService.getErrorUrl(this, relayer) { str ->
            test_string.text = str
        }
    }
}
