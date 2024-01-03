package org.sopt.dosoptjaewon.presentation.signup

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.data.network.model.signup.SignupRequest
import org.sopt.dosoptjaewon.data.network.repository.signup.SignupRepository
import retrofit2.Response


class SignupViewModel(private val signupRepository: SignupRepository) : ViewModel() {

    val signupState = MutableStateFlow<SignupState>(SignupState.Idle)

    val userId = MutableLiveData<String>()
    val userPw = MutableLiveData<String>()
    val userNickname = MutableLiveData<String>()
    val userHobby = MutableLiveData<String>()

    val idError = MediatorLiveData<String?>().apply {
        addSource(userId) { validateId() }
    }

    val passwordError = MediatorLiveData<String?>().apply {
        addSource(userPw) { validatePassword() }
    }

    val isSignupEnable = MediatorLiveData<Boolean>().apply {
        addSource(userId) { signUpVaildForm() }
        addSource(userPw) { signUpVaildForm() }
        addSource(userNickname) { signUpVaildForm() }
        addSource(userHobby) { signUpVaildForm() }
    }

    private fun validateId() {
        idError.value =
            if (userId.value.isNullOrEmpty() || (userId.value!!.matches(idRegex) &&
                        userId.value!!.length in MIN_ID_LENGTH..MAX_ID_LENGTH)
            ) {
                null
            } else {
                ID_CONDITION_MESSAGE
            }
    }

    private fun validatePassword() {
        passwordError.value =
            if (userPw.value.isNullOrEmpty() || (userPw.value!!.matches(passwordRegex) &&
                        userPw.value!!.length in MIN_PW_LENGTH..MAX_PW_LENGTH)
            ) {
                null
            } else {
                PASSWORD_CONDITION_MESSAGE
            }
    }

    private fun signUpVaildForm() {
        val user = User(
            userId.value ?: "",
            userPw.value ?: "",
            userNickname.value ?: "",
            userHobby.value ?: ""
        )
        isSignupEnable.value = signupValidCheck(user)
    }

    fun postSignup() {
        viewModelScope.launch {
            runCatching {
                signupRepository.signup(
                    SignupRequest(
                        userId.value!!,
                        userPw.value!!,
                        userNickname.value!!
                    )
                )
            }.onSuccess { processSuccess(it) }
                .onFailure { processFailure(it) }
        }
    }

    private fun processSuccess(response: Response<Unit>) {
        if (response.isSuccessful) {
            signupState.value = SignupState.Success(userId.value ?: "")
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

    private fun signupValidCheck(user: User): Boolean {
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
        private const val ID_CONDITION_MESSAGE = "아이디는 6자 이상 10자 이하 이며, 영문과 숫자를 포함해야 합니다."
        private const val PASSWORD_CONDITION_MESSAGE =
            "비밀번호는 6자 이상 12자 이하 이며, 숫자와 특수 문자를 포함해야 합니다."

        val idRegex = Regex(ID_PATTERN)
        val passwordRegex = Regex(PASSWORD_PATTERN)
        val nicknameRegex = Regex(NICKNAME_PATTERN)
        val hobbyRegex = Regex(HOBBY_PATTERN)
    }

}
