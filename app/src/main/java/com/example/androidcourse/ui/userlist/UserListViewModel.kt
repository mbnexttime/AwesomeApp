package com.example.androidcourse.ui.userlist

import android.service.autofill.UserData
import androidx.lifecycle.viewModelScope
import com.example.androidcourse.contacts.User
import com.example.androidcourse.infra.BaseViewModel
import com.example.androidcourse.net.Api
import com.haroldadmin.cnradapter.NetworkResponse
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val api_: Lazy<Api>,
) : BaseViewModel() {
    private val api: Api
        get() {
            return api_.get()
        }
    private val uiState_: MutableStateFlow<ViewState> = MutableStateFlow(ViewState.Loading)

    val uiState: Flow<ViewState>
        get() = uiState_.asStateFlow()
    var currentFilter: String? = null

    init {
        viewModelScope.launch {
            uiState_.emit(ViewState.Loading)
            when (val response = api.getUsers()) {
                is NetworkResponse.Success -> {
                    if (response.body.isEmpty()) {
                        uiState_.emit(ViewState.Empty)
                    } else {
                        uiState_.emit(ViewState.Data(response.body))
                    }
                }
                else -> {
                    uiState_.emit(ViewState.Error)
                }
            }
        }
    }

    sealed class ViewState {
        object Loading : ViewState()
        data class Data(val userList: List<User>) : ViewState()
        object Empty : ViewState()
        object Error : ViewState()
    }
}