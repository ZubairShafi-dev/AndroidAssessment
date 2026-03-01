package com.example.androidassessment.util

import com.example.androidassessment.BuildConfig
import timber.log.Timber

/**
 * Centralized logging utility backed by Timber.
 *
 * All methods are no-ops when [BuildConfig.IS_LOGGING_ENABLED] is false,
 * which means zero log output in production / release builds.
 *
 * Usage:
 *   AppLogger.d("MyTag", "something happened")
 *   AppLogger.e("MyTag", "oops", exception)
 */
object AppLogger {

    // ── Public API ────────────────────────────────────────────────────────────

    /** Verbose-level debug message. */
    fun d(tag: String, message: String) {
        if (loggingEnabled()) Timber.tag(tag).d(message)
    }

    /** Info-level message. */
    fun i(tag: String, message: String) {
        if (loggingEnabled()) Timber.tag(tag).i(message)
    }

    /** Warning-level message. */
    fun w(tag: String, message: String) {
        if (loggingEnabled()) Timber.tag(tag).w(message)
    }

    /** Error-level message with an optional [throwable]. */
    fun e(tag: String, message: String, throwable: Throwable? = null) {
        if (loggingEnabled()) Timber.tag(tag).e(throwable, message)
    }

    // ── Internal ──────────────────────────────────────────────────────────────

    /**
     * Checked inline to avoid any Timber call-site overhead when logging is
     * disabled. ProGuard's `-assumenosideeffects` rule strips this entirely
     * from release bytecode.
     */
    private fun loggingEnabled(): Boolean = BuildConfig.IS_LOGGING_ENABLED
}
