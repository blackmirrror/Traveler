package ru.blackmirrror.core.provider

interface NetworkProvider {
    fun isInternetConnection(): Boolean
}