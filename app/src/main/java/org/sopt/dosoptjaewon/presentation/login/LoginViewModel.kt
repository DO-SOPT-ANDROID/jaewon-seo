package org.sopt.dosoptjaewon.presentation.login

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.dosoptjaewon.data.network.model.login.LoginRequest
import org.sopt.dosoptjaewon.data.network.model.login.LoginResponse
import org.sopt.dosoptjaewon.data.network.repository.login.LoginRepository

class LoginViewModel(private val loginRepository: LoginRepository) : ViewModel() {
    val loginState = MutableLiveData<LoginState>(LoginState.Idle)

    fun handleLogin(id: String, pw: String) {
        viewModelScope.launch {
            runCatching {
                loginRepository.login(LoginRequest(id, pw))
            }.onSuccess { processSuccess(it) }
                .onFailure { processFailure(it) }
        }
    }

    private fun processSuccess(response: LoginResponse) {
        loginState.value = LoginState.Success(response.memberId)
    }

    private fun processFailure(exception: Throwable) {
        loginState.value = LoginState.Failure(exception.message ?: DEFAULT_ERROR_MESSAGE)
    }

    companion object {
        private const val DEFAULT_ERROR_MESSAGE = "로그인에 실패하였습니다."
    }
}