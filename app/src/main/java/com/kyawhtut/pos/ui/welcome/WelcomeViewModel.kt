package com.kyawhtut.pos.ui.welcome

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

/**
 * @author kyawhtut
 * @date 25/05/2020
 */
class WelcomeViewModel(private val repo: WelcomeRepository) :
    ViewModel() {

    private val _state by lazy { MutableLiveData<State>() }
    fun getState() = _state

    val isDeviceCheck: Boolean
        get() = repo.isDeviceCheck

    var phone: String = repo.loginPhone

    fun check() {
        viewModelScope.launch {
            repo.checkDevice(phone) {
                _state.postValue(it)
            }
        }
    }


    sealed class State {
        object LOADING : State()
        data class FINISH(val status: Int, val message: String) : State()
        data class ERROR(val status: Int, val error: String) : State()
    }
}
