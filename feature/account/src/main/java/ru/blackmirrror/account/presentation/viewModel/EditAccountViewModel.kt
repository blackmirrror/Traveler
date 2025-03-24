package ru.blackmirrror.account.presentation.viewModel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import ru.blackmirrror.navigator.TravelerNavigator
import javax.inject.Inject

@HiltViewModel
class EditAccountViewModel @Inject constructor(
    private val travelerNavigator: TravelerNavigator,
) : ViewModel(), TravelerNavigator by travelerNavigator