package com.example.androidcourse.ui.signup

import androidx.lifecycle.viewModelScope
import com.example.androidcourse.auth.AuthInteractor
import com.example.androidcourse.auth.AuthRepository
import com.example.androidcourse.infra.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val authInteractor: AuthInteractor,
) : BaseViewModel() {

    private val _eventChannel = Channel<Event>(Channel.BUFFERED)

    fun eventsFlow(): Flow<Event> {
        return _eventChannel.receiveAsFlow()
    }

    fun signUp(
        firstname: String,
        lastname: String,
        nickname: String,
        email: String,
        password: String
    ) {
        viewModelScope.launch {
            try {
                authInteractor.signUp(
                    firstname,
                    lastname,
                    nickname,
                    email,
                    password
                )
                _eventChannel.send(Event.SignUpEmailConfirmationRequired)
            } catch (error: Exception) {
                _eventChannel.send(Event.SignUpError)
            }
        }
    }

    sealed class Event {
        object SignUpEmailConfirmationRequired : Event()
        object SignUpError : Event()
    }
}