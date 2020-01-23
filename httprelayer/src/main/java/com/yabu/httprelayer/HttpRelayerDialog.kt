package com.yabu.httprelayer

import android.content.Context
import android.content.res.ColorStateList
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.http_relayer_dialog.view.*

// constants

/**
 * Exit translation amount.
 */
const val EXIT_Y_TRANSLATION = 200f

/**
 * A [HttpRelayer] dialog to display the debugging of the http call performed.
 * Every instance
 * @property context [Context] of this [HttpRelayerDialog].
 * @property startTime [Long] Time of request in nano seconds.
 * @property endTime [Long] Time of response in nano seconds.
 * @see HttpRelayer
 */
class HttpRelayerDialog(val context: Context) {

    /**
     * startTime [Long] Time of request in nano seconds.
     */
    var startTime: Long = 0

    /**
     * endTime [Long] Time of response in nano seconds.
     */
    var endTime: Long = 0

    /**
     * [HttpRelayerRequest] assigned to this dialog.
     */
    private var request: HttpRelayerRequest = HttpRelayerRequest()

    /**
     * Toggle to check if dialog is expanded.
     */
    private var isExpanded = false

    /**
     * Simple calculator to get the HTTP call total time from [startTime] and [endTime]
     * in measure of milliseconds.
     */
    private val httpCallTimeMilliSeconds
        get() = (endTime - startTime) / 1e6

    /**
     * Interface to listen for HTTP intercepted calls.
     */
    interface InterceptListener {
        fun onInterceptRequest(request: HttpRelayerRequest)
        fun onInterceptResponse(response: HttpRelayerResponse)
    }

    /**
     * Reference to this [HttpRelayerDialog] dialog view layout.
     */
    private var dialogView: View? = null

    /**
     * Reference to a listener to pass the callbacks of the HTTP call.
     */
    var listener: InterceptListener? = null

    /**
     * Creates the dialog and listener to inflate this [HttpRelayerDialog].
     * @return [HttpRelayerDialog]
     */
    fun create(): HttpRelayerDialog {
        // generate a layout,
        generateDialog()

        // set the listener,
        listener = object : InterceptListener {
            override fun onInterceptRequest(request: HttpRelayerRequest) {
                // log the request time,
                startTime = System.nanoTime()
                // on request,
                this@HttpRelayerDialog.request = request
                // display the dialog,
                display()
            }

            override fun onInterceptResponse(response: HttpRelayerResponse) {
                // log the response time
                endTime = System.nanoTime()
                dialogView?.also { v ->
                    // switch containers to show response views,
                    v.http_relayer_ongoing_container.gone()
                    v.http_relayer_response_container.showAnimated()
                }

                // on response code,
                when (response.code) {
                    in 100 until 200 -> toggleInformation(response)
                    in 200 until 300 -> toggleSuccess(response)
                    in 300 until 400 -> toggleRedirect(response)
                    in 400 until 500 -> toggleError(response)
                    in 500 until 600 -> toggleError(response)
                }
            }
        }

        return this
    }

    /**
     * Switches the dialog display to a success message.
     */
    private fun toggleInformation(response: HttpRelayerResponse) {
        dialogView?.also { v ->
            // set the color and text views,
            v.http_relayer_parent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.http_relayer_blue))
            v.http_relayer_status_code.text = response.code.toString()
            v.http_relayer_message.text = response.message

            // set the information text,
            v.http_relayer_request_method.text = request.method
            v.http_relayer_request_endpoint.text = request.endpoint
        }
    }

    /**
     * Switches the dialog display to a success message.
     */
    private fun toggleSuccess(response: HttpRelayerResponse) {
        dialogView?.also { v ->
            // set the color and text views,
            v.http_relayer_parent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.http_relayer_green))
            // switch icon
            v.http_relayer_response_icon.setImageResource(R.drawable.ic_check_circle_black_24dp)
            v.http_relayer_status_code.text = response.code.toString()
            v.http_relayer_message.text = response.message

            // set total time
            v.http_relayer_request_method.text = request.method
            v.http_relayer_request_endpoint.text = request.endpoint
            v.http_relayer_time.text = context.getString(R.string.http_relayer_time_template, httpCallTimeMilliSeconds)
        }
    }

    /**
     * Switches the dialog display to a success message.
     */
    private fun toggleRedirect(response: HttpRelayerResponse) {
        dialogView?.also { v ->
            // set the color and text views,
            v.http_relayer_response_icon.setImageResource(R.drawable.ic_check_circle_black_24dp)
            v.http_relayer_parent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.http_relayer_blue))
            v.http_relayer_status_code.text = response.code.toString()
            v.http_relayer_message.text = response.message

            // set total time
            v.http_relayer_request_method.text = request.method
            v.http_relayer_request_endpoint.text = request.endpoint
            v.http_relayer_time.text = context.getString(R.string.http_relayer_time_template, httpCallTimeMilliSeconds)
        }
    }

    /**
     * Switches the dialog display to an error message.
     */
    private fun toggleError(response: HttpRelayerResponse) {
        dialogView?.also { v ->
            // set the color and text views,
            v.http_relayer_response_icon.setImageResource(R.drawable.ic_close_circle)
            v.http_relayer_parent.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(context, R.color.http_relayer_red))
            v.http_relayer_status_code.text = response.code.toString()
            v.http_relayer_message.text = response.message

            // set total time
            v.http_relayer_request_method.text = request.method
            v.http_relayer_request_endpoint.text = request.endpoint
            v.http_relayer_time.text = context.getString(R.string.http_relayer_time_template, httpCallTimeMilliSeconds)
        }
    }

    /**
     * Displays the dialog in the view to the user with animation on a request intercepted.
     * @return [HttpRelayerDialog]
     */
    private fun display(): HttpRelayerDialog {
        dialogView?.also { v ->
            v.showAnimatedWithTranslation()
        }
        return this
    }

    /**
     * Sets the [HttpRelayerDialog] to display a verbose version of the dialog,
     * showing a larger dialog and more HTTP information than the default status code and message display.
     * @return [HttpRelayerDialog]
     */
    fun setVerbose(): HttpRelayerDialog {
        dialogView?.also { v ->
            v.http_relayer_expand_button.show()
            v.http_relayer_expand_button.setOnClickListener {
                if (!isExpanded) {
                    val lp = v.layoutParams
                    lp.height = context.resources.getDimension(R.dimen.http_relayer_expanded_size).toInt()
                    v.layoutParams = lp
                    v.http_relayer_time.showAnimated()
                    v.http_relayer_request_method.showAnimated()
                    v.http_relayer_request_endpoint.showAnimated()
                    v.http_relayer_expand_button.animate().rotation(180f)
                }
                else {
                    val lp = v.layoutParams
                    lp.height = context.resources.getDimension(R.dimen.http_relayer_default_size).toInt()
                    v.layoutParams = lp
                    v.http_relayer_time.gone()
                    v.http_relayer_request_method.gone()
                    v.http_relayer_request_endpoint.gone()
                    v.http_relayer_expand_button.animate().rotation(0f)
                }
                isExpanded = !isExpanded
            }
        }
        return this
    }

    /**
     * Inflates a [HttpRelayerDialog] view in the corner of the screen with the default progress views visible.
     */
    private fun generateDialog() {
        val resources = context.resources
        // get the size,
        val dimension = resources.getDimension(R.dimen.http_relayer_default_size)
        // margin
        val margin = resources.getDimension(R.dimen.margin)
        // parent of activity,
        val parent = (context as AppCompatActivity).window.decorView.rootView as? ViewGroup
        // inflate view,
        dialogView = LayoutInflater.from(context).inflate(R.layout.http_relayer_dialog, parent, false)

        dialogView?.also { v ->
            // set alpha to 0
            v.hide()
            // set close button
            v.http_relayer_close_button.setOnClickListener {
                v.animate()
                    .translationY(EXIT_Y_TRANSLATION)
                    .alpha(0f)
                    .start()
            }

            // set the coordinates to be on the bottom right of the screen,
            val (screenWidth, screenHeight) = screenDimension.first to screenDimension.second

            // navigation bar height
            val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
            val navHeight = if (resourceId > 0) resources.getDimensionPixelSize(resourceId) else 0

            val params = FrameLayout.LayoutParams(dimension.toInt(), dimension.toInt())
            params.leftMargin = screenWidth - dimension.toInt() - margin.toInt()
            params.topMargin = screenHeight - dimension.toInt() - margin.toInt() - navHeight
            // add the view to the parent container,
            parent?.addView(dialogView, params)
        }
    }
}