package org.sopt.dosoptjaewon.presentation.login

sealed class LoginState {
    data class Success(val server_id: Int) : LoginState()
    data class Failure(val message: String) : LoginState()
    object Loading : LoginState()
    object Idle : LoginState()
}