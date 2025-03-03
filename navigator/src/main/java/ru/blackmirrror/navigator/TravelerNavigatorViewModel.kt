package ru.blackmirrror.navigator

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TravelerNavigatorViewModel @Inject constructor(
        private val travelerNavigator: TravelerNavigator
) : ViewModel(), TravelerNavigator by travelerNavigator