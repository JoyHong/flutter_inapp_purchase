package com.dooboolab.flutterinapppurchase

import android.os.Handler
import io.flutter.plugin.common.MethodChannel
import java.lang.Runnable
import android.os.Looper

// MethodChannel.Result wrapper that responds on the platform thread.
class MethodResultWrapper internal constructor(
    private val safeResult: MethodChannel.Result,
    private val safeChannel: MethodChannel
) : MethodChannel.Result {
    private val handler: Handler = Handler(Looper.getMainLooper())
    override fun success(result: Any?) {
        handler.post {
            try {
                safeResult.success(result)
            } catch (ignored: IllegalStateException) {
                // Reply already submitted
            }
        }
    }

    override fun error(errorCode: String, errorMessage: String?, errorDetails: Any?) {
        handler.post {
            try {
                safeResult.error(errorCode, errorMessage, errorDetails)
            } catch (ignored: IllegalStateException) {
                // Reply already submitted
            }
        }
    }

    override fun notImplemented() {
        handler.post {
            try {
                safeResult.notImplemented()
            } catch (ignored: IllegalStateException) {
                // Reply already submitted
            }
        }
    }

    fun invokeMethod(method: String?, arguments: Any?) {
        handler.post {
            try {
                safeChannel.invokeMethod(method!!, arguments, null)
            } catch (ignored: IllegalStateException) {
                // Reply already submitted
            }
        }
    }

}