package com.fadhil.storyapp.data

sealed class Result<out T>(val data: T? = null, val code: String? = null, val message: String? = null) {

    open var status: Status = Status.LOADING

    sealed class Status {
        object SUCCESS : Status()
        object UNAUTHORIZED: Status()
        object ERROR : Status()
        object LOADING : Status()
    }

    class Success<T>(data: T?) : Result<T>(data) {
        override var status: Status = Status.SUCCESS
    }

    class Unauthorized<T>(message: String?, data: T? = null) : Result<T>(data, "401", message) {
        override var status: Status = Status.UNAUTHORIZED
    }

    class Error<T>(code: String?, message: String?) : Result<T>(null, code, message) {
        override var status: Status = Status.ERROR
    }

    class Loading<T>(data: T? = null) : Result<T>(data) {
        override var status: Status = Status.LOADING
    }

}