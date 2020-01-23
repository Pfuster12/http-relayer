package com.yabu.httprelayer

import android.content.Context

// constants

/**
 * A Shared Preference Key to check if [HttpRelayer] should generate a dialog.
 */
const val HTTP_RELAYER_PREFERENCE_KEY = "httprelayer.HTTP_RELAYER_PREFERENCE_KEY"

/**
 * This is a debugging Android library to display in the bottom-right corner of the current
 * context screen the status of an ongoing http call, and display the response code and message
 * received from a HTTP call that occurs only in the current screen.
 *
 * Once the HTTP call is launched, the dialog displays appears and displays a small set of information on
 * the current call being launched and a loading indicator. Once the call is done, the dialog changes to
 * display the result:
 *   - On 2XX codes, the dialog will display a green background to signal the OK status,
 *     and any error codes will display a red background to signal a failure.
 *
 * The dialog is used for debugging, so the only way to remove it from view is to explicitly close it
 * with the close button on its top-right. This way testers can screenshot the result of an http that needs attention.
 * @see HttpRelayerDialog
 */
object HttpRelayer {

    /**
     * Provides the context screen parent view to add the dialog to.
     * Creates a [HttpRelayerDialog]
     */
    fun with(context: Context): HttpRelayerDialog? {
        // check if relayer is set in the preferences,
        val isRelayerOn = context.getSharedPreferences("test", Context.MODE_PRIVATE)
            .getBoolean(HTTP_RELAYER_PREFERENCE_KEY, false)
        // init a null dialog,
        var dialog: HttpRelayerDialog? = null

        if (isRelayerOn) {
            // create a new dialog
            dialog = HttpRelayerDialog(context)
        }
        // return a dialog if preference is on,
        return dialog
    }
}

/**
 * Data class for holding a http request object.
 */
data class HttpRelayerRequest(val endpoint: String = "",
                              val method: String = "GET",
                              val headers: String = "")

/**
 * Data class for holding a http response object.
 */
data class HttpRelayerResponse(val request: HttpRelayerRequest = HttpRelayerRequest(),
                               val code: Int? = 0,
                               val message: String? = "")