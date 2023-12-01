package org.sopt.dosoptjaewon.presentation.signup

import org.sopt.dosoptjaewon.data.model.User

sealed class SignupState {
    data class Success(val userId: String) : SignupState()
    data class Failure(val message: String) : SignupState()
    object Idle : SignupState()
}