package ru.blackmirrror.core.api

interface NetworkProvider {
    fun isInternetConnection(): Boolean
}