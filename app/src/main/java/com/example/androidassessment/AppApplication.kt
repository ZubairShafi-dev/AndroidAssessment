package com.example.androidassessment

import android.app.Application
import com.example.androidassessment.util.AppLogger
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Entry point for Hilt and app-wide initialization.
 *
 * Timber is planted **only** when [BuildConfig.IS_LOGGING_ENABLED] is true
 * (i.e. dev and mock flavors). The release / prod flavor never plants a tree,
 * so even if Timber call sites somehow survive ProGuard they will silently
 * no-op via the [AppLogger] guard.
 */
@HiltAndroidApp
class AppApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogging()
    }

    // ─────────────────────────────────────────────────────────────────────────

    private fun initLogging() {
        if (BuildConfig.IS_LOGGING_ENABLED) {
            Timber.plant(Timber.DebugTree())
            AppLogger.d(TAG, "Timber initialized — logging is ON (${BuildConfig.BUILD_TYPE})")
        }
        // In release/prod: no tree is planted → Timber and AppLogger are both silent
    }

    companion object {
        private const val TAG = "AppApplication"
    }
}
