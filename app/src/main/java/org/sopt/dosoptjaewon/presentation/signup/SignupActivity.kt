package org.sopt.dosoptjaewon.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sopt.common.context.hideKeyboard
import com.sopt.common.context.stringOf
import com.sopt.common.context.toast
import com.sopt.common.view.snackBar
import org.sopt.dosoptjaewon.R.string
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivitySignupBinding
import org.sopt.dosoptjaewon.presentation.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        with(binding) {
            btnSignupComplete.setOnClickListener { handleSignupCompleteClick() }
        }
    }

    private fun handleSignupCompleteClick() {
        // 회원가입 정보를 담은 User 객체 생성
        val user = User(
            id = binding.etSignupId.text.toString(),
            pw = binding.etSignupPw.text.toString(),
            nickname = binding.etSignupNickname.text.toString(),
            hobby = binding.etSignupHobby.text.toString()
        )

        // 회원가입 형식이 맞으면 로그인 액티비티로 이동
        if (signupValid(user)) {
            toast(stringOf(string.signup_success))
            navigateToLogin(user)
        } else {
            binding.root.snackBar(stringOf(string.signup_fail))
        }
    }

    private fun navigateToLogin(user: User) {
        // 회원가입 정보를 user data class에 담아 로그인 액티비티로 전달
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        intent.putExtra(EXTRA_DATA, user)
        setResult(RESULT_OK, intent)
        finish()
    }

    // 회원가입 유효성 확인
    private fun signupValid(user: User): Boolean {
        return user.id.length in MIN_ID_LENGTH..MAX_ID_LENGTH &&
                user.pw.length in MIN_PW_LENGTH..MAX_PW_LENGTH &&
                user.nickname.isNotBlank() && user.nickname.matches(nicknameRegex) &&
                user.hobby.isNotBlank() && user.hobby.matches(hobbyRegex)
    }

    // 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val EXTRA_DATA = "user_data"

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