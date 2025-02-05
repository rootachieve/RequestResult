package com.rootachieve.requestresult

import com.rootachieve.requestresult.RequestResult.Failure
import com.rootachieve.requestresult.RequestResult.None
import com.rootachieve.requestresult.RequestResult.Progress
import com.rootachieve.requestresult.RequestResult.Success
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Represents the result of a request, encapsulating various states: [Success], [Failure], [Progress], and [None].
 * This helps in managing and handling request states in a structured way.
 */
sealed class RequestResult<out T> {
    private var isUsed = AtomicBoolean(false)
    // Prevents multiple execution of success/failure handlers by default

    /**
     * Represents a state where no request has been made or initialized.
     */
    class None : RequestResult<Nothing>() {
        override fun toString(): String {
            return "State(None)"
        }
    }

    /**
     * It represents a state where the request is in progress.
     */
    class Progress : RequestResult<Nothing>() {
        override fun toString(): String {
            return "State(Progress)"
        }
    }

    /**
     * It represents a state where the request has succeeded and holds data of type [T].
     * @param data The result value of the request.
     */
    data class Success<T>(val data: T) : RequestResult<T>() {
        override fun toString(): String {
            return "State(Success, data=${data.toString()})"
        }
    }

    /**
     * It represents a state where the request has failed,
     * holding an error code of type [String] and an exception of type [Throwable].
     * @param code
     * This is the error code for the failure. It allows error handling based on different conditions.
     * @param exception
     * This is the exception for the failure. The exception can either be passed as-is or contain a custom message provided by the user.
     */
    data class Failure(val code: String, val exception: Throwable? = null) :
        RequestResult<Nothing>() {
        override fun toString(): String {
            return "State(Failure, code=$code, exception=${exception.toString()})"
        }
    }

    fun getOrNull(): T? {
        return if (this is Success) this.data
        else null
    }

    fun isSuccess(): Boolean = this is Success
    fun isFailure(): Boolean = this is Failure
    fun isNone(): Boolean = this is None
    fun isProgress(): Boolean = this is Progress
    fun isUsed(): Boolean = isUsed.get()


    /**
     * Executes the [running] lambda if the result is [Success]. By default, the lambda runs only once per instance.
     *
     * @param reuse If true, allows the lambda to run multiple times. Default is false.
     * @param running The task to execute with the successful result data.
     * @see onFailure
     * @see onNone
     * @see onProgress
     */
    suspend fun onSuccess(reuse: Boolean = false, running: suspend (T) -> Unit): RequestResult<T> {
        if (this is Success && (reuse || isUsed.get())) {
            running(this.data)
            isUsed.set(true)
        }
        return this
    }

    /**
     * Executes the [running] lambda if the result is [Failure]. The lambda runs only once per instance unless [reuse] is true.
     *
     * @param reuse Allows multiple executions of the lambda if true. Default is false.
     * @param running The task to execute with the error code and exception.
     * @see onSuccess
     * @see onNone
     * @see onProgress
     */
    suspend fun onFailure(
        reuse: Boolean = false,
        running: suspend (String, Throwable?) -> Unit,
    ): RequestResult<T> {
        if (this is Failure && (reuse || isUsed.get())) {
            running(this.code, this.exception)
            isUsed.set(true)
        }
        return this
    }

    /**
     * Executes the [running] lambda if the result is [Progress]. The lambda runs only once per instance unless [reuse] is true.
     *
     * @param reuse Allows multiple executions of the lambda if true. Default is false.
     * @param running The task to execute when the request state is [Progress].
     * @see onSuccess
     * @see onFailure
     * @see onNone
     */
    suspend fun onProgress(reuse: Boolean = false, running: suspend () -> Unit): RequestResult<T> {
        if (this is Progress && (reuse || isUsed.get())) {
            running()
            isUsed.set(true)
        }
        return this
    }

    /**
     * Executes the [running] lambda if the result is [None]. The lambda runs only once per instance unless [reuse] is true.
     *
     * @param reuse Allows multiple executions of the lambda if true. Default is false.
     * @param running The task to execute when the request state is [None].
     * @see onSuccess
     * @see onFailure
     * @see onProgress
     */
    suspend fun onNone(reuse: Boolean = false, running: suspend () -> Unit): RequestResult<T> {
        if (this is None && (reuse || isUsed.get())) {
            running()
            isUsed.set(true)
        }
        return this
    }
}