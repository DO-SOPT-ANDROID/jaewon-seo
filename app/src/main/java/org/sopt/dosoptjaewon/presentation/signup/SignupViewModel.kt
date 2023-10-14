package org.sopt.dosoptjaewon.presentation.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.sopt.dosoptjaewon.data.model.User

sealed class SignupState {
    data class Success(val user: User) : SignupState()
    object Failure : SignupState()
    object Idle : SignupState()
}


class SignupViewModel : ViewModel() {
    val signupState = MutableLiveData<SignupState>(SignupState.Idle)

    fun handleSignup(user: User) {
        if (signupValid(user)) {
            signupState.value = SignupState.Success(user)
        } else {
            signupState.value = SignupState.Failure
        }
    }

    private fun signupValid(user: User): Boolean {
        return user.id.length in MIN_ID_LENGTH..MAX_ID_LENGTH &&
                user.pw.length in MIN_PW_LENGTH..MAX_PW_LENGTH &&
                user.nickname.isNotBlank() && user.nickname.matches(nicknameRegex) &&
                user.hobby.isNotBlank() && user.hobby.matches(hobbyRegex)
    }

    companion object {
        //회원가입 조건
        private const val MIN_ID_LENGTH = 6
        private const val MAX_ID_LENGTH = 10
        private const val MIN_PW_LENGTH = 8
        private const val MAX_PW_LENGTH = 12

        //정규식 패턴
        private const val NICKNAME_PATTERN = "^[a-zA-Z0-9가-힣]*$"
        private const val HOBBY_PATTERN = "^[a-zA-Z0-9가-힣]*$"
        val nicknameRegex = Regex(NICKNAME_PATTERN)
        val hobbyRegex = Regex(HOBBY_PATTERN)
    }
}
