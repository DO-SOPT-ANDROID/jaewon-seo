package org.sopt.dosoptjaewon.presentation.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.dosoptjaewon.data.model.User

class MainViewModel : ViewModel() {
    // 로그인 정보를 담은 User 객체를 LiveData로 관리
    private val _userInfo = MutableLiveData<User>()
    val userInfo: LiveData<User> get() = _userInfo

    fun setUserInfo(user: User) {
        _userInfo.value = user
    }
}