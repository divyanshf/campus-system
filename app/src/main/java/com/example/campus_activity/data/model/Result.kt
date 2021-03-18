package com.example.campus_activity.data.model

sealed class Result<out T : Any?>{
    sealed class Success{
        data class ChatAdd<out R>(val result:R) : Result<R>()
        data class ChatLoad<out R>(val result:R) : Result<R>()
        data class Success<out R>(val result:R) : Result<R>()
    }
    data class Error(val message: String) : Result<Nothing>()
    object Progress : Result<Nothing>()
}