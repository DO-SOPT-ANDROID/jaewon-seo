package org.sopt.dosoptjaewon.presentation.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.data.network.model.signup.SignupRequest
import org.sopt.dosoptjaewon.data.network.repository.signup.SignupRepository
import retrofit2.Response


class SignupViewModel(private val signupRepository: SignupRepository) : ViewModel() {

    val signupState = MutableLiveData<SignupState>(SignupState.Idle)

    fun handleSignup(user: User) {
        viewModelScope.launch {
            runCatching {
                signupRepository.signup(SignupRequest(user.id, user.pw, user.nickname))
            }.onSuccess { processSuccess(it, user) }
                .onFailure { processFailure(it) }
        }
    }

    private fun processSuccess(response: Response<Unit>, user: User) {
        if (response.isSuccessful) {
            signupState.value = SignupState.Success(user)
        } else {
            val errorMessage =
                extractErrorMessage(response.errorBody()?.string()) ?: DEFAULT_ERROR_MESSAGE
            signupState.value = SignupState.Failure(errorMessage)
        }
    }

    private fun processFailure(exception: Throwable) {
        signupState.value = SignupState.Failure(exception.message ?: DEFAULT_ERROR_MESSAGE)
    }

    private fun extractErrorMessage(jsonString: String?): String? {
        return try {
            val jsonObject = Json.parseToJsonElement(jsonString ?: "").jsonObject
            jsonObject["message"]?.jsonPrimitive?.content
        } catch (e: Exception) {
            null
        }
    }

    fun signupValidCheck(user: User): Boolean {
        return user.id.length in MIN_ID_LENGTH..MAX_ID_LENGTH &&
                user.id.matches(idRegex) &&
                user.pw.length in MIN_PW_LENGTH..MAX_PW_LENGTH &&
                user.pw.matches(passwordRegex) &&
                user.nickname.isNotBlank() && user.nickname.matches(nicknameRegex) &&
                user.hobby.isNotBlank() && user.hobby.matches(hobbyRegex)
    }

    companion object {
        private const val MIN_ID_LENGTH = 6
        private const val MAX_ID_LENGTH = 10
        private const val MIN_PW_LENGTH = 6
        private const val MAX_PW_LENGTH = 12
        private const val NICKNAME_PATTERN = "^[a-zA-Z0-9가-힣]*$"
        private const val HOBBY_PATTERN = "^[a-zA-Z0-9가-힣]*$"
        private const val DEFAULT_ERROR_MESSAGE = "회원가입에 실패하였습니다."

        private const val ID_PATTERN = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]+$"
        private const val PASSWORD_PATTERN =
            "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@\$!%*?&])[A-Za-z\\d@\$!%*?&]+$"

        val idRegex = Regex(ID_PATTERN)
        val passwordRegex = Regex(PASSWORD_PATTERN)
        val nicknameRegex = Regex(NICKNAME_PATTERN)
        val hobbyRegex = Regex(HOBBY_PATTERN)
    }

}
