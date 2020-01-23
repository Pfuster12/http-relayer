package com.yabu.httprelayerexample.data

import android.content.Context
import android.os.Handler
import com.yabu.httprelayer.HttpRelayerDialog
import com.yabu.httprelayer.HttpRelayerRequest
import com.yabu.httprelayer.HttpRelayerResponse
import okhttp3.*
import java.io.IOException

class HttpService {
    companion object {
        private const val TEST_URL = "https://httpbin.org/get"
        private const val ERROR_TEST_URL = "http://www.mocky.io/v2/5e29a1a8300000cd68faf19b"

        /**
         * GET test url.
         */
        fun getTestUrl(context: Context, relayer: HttpRelayerDialog?, callback: (str: String) -> Unit) {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(TEST_URL)
                .build()
            val relayReq = HttpRelayerRequest(TEST_URL)
            relayer?.listener?.onInterceptRequest(relayReq)

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val handler = Handler(context.mainLooper)
                    handler.post {
                        relayer?.listener?.onInterceptResponse(
                            HttpRelayerResponse(
                                relayReq,
                                400,
                                e.message))
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val handler = Handler(context.mainLooper)
                    handler.post {
                        callback.invoke(response.body?.string() ?: "")
                        relayer?.listener?.onInterceptResponse(
                            HttpRelayerResponse(
                                relayReq,
                                response.code,
                                response.message
                            )
                        )
                    }
                }
            })
        }
        /**
         * GET test url.
         */
        fun getErrorUrl(context: Context, relayer: HttpRelayerDialog?, callback: (str: String) -> Unit) {
            val client = OkHttpClient()
            val request: Request = Request.Builder()
                .url(ERROR_TEST_URL)
                .build()
            val relayReq = HttpRelayerRequest(ERROR_TEST_URL)
            relayer?.listener?.onInterceptRequest(relayReq)

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    val handler = Handler(context.mainLooper)
                    handler.post {
                        relayer?.listener?.onInterceptResponse(
                            HttpRelayerResponse(
                                relayReq,
                                400,
                                e.message))
                    }
                }

                override fun onResponse(call: Call, response: Response) {
                    val handler = Handler(context.mainLooper)
                    handler.post {
                        callback.invoke(response.body?.string() ?: "")
                        relayer?.listener?.onInterceptResponse(
                            HttpRelayerResponse(
                                relayReq,
                                response.code,
                                response.message
                            )
                        )
                    }
                }
            })
        }

    }
}