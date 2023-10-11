package org.sopt.dosopttemplate.presentation.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.dosopttemplate.data.model.User

class LoginViewModel : ViewModel() {
    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> get() = _userInfo

    fun setUserInfo(id: String, pw: String, nickname: String, hobby: String) {
        _userInfo.value = User(id, pw, nickname, hobby)
    }
}