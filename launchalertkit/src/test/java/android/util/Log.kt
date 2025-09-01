package android.util/*
 *  File: Log.kt
 *
 *  Created by morteza on 8/31/25.
 */

/**
 * Stub implementation of android.util.Log for JVM unit tests.
 * Prevents "Method isLoggable not mocked" runtime exceptions.
 */
object Log {
    const val VERBOSE = 2
    const val DEBUG = 3
    const val INFO = 4
    const val WARN = 5
    const val ERROR = 6
    const val ASSERT = 7

    @JvmStatic fun v(tag: String?, msg: String?): Int = 0
    @JvmStatic fun v(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic fun d(tag: String?, msg: String?): Int = 0
    @JvmStatic fun d(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic fun i(tag: String?, msg: String?): Int = 0
    @JvmStatic fun i(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic fun w(tag: String?, msg: String?): Int = 0
    @JvmStatic fun w(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic fun e(tag: String?, msg: String?): Int = 0
    @JvmStatic fun e(tag: String?, msg: String?, tr: Throwable?): Int = 0

    @JvmStatic fun isLoggable(tag: String?, level: Int): Boolean = false

    @JvmStatic fun getStackTraceString(tr: Throwable?): String = tr?.stackTraceToString() ?: ""
}
