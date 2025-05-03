package ru.blackmirrror.core.api

import android.app.Activity

interface ActivityProvider {
    fun getActivity(): Activity
}