package org.sopt.dosoptjaewon.presentation.signup

sealed class SignupState {
    data class Success(val userId: String) : SignupState()
    data class Failure(val message: String) : SignupState()
    object Idle : SignupState()
}