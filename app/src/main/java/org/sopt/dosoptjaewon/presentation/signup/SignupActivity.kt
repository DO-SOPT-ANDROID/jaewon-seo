package org.sopt.dosoptjaewon.presentation.signup

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivitySignupBinding
import org.sopt.dosoptjaewon.presentation.login.LoginActivity
import org.sopt.dosoptjaewon.util.snackbar
import org.sopt.dosoptjaewon.util.toast

class SignupActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setOnClickListener()
    }

    private fun setOnClickListener() {
        with(binding) {
            // 회원가입 완료 버튼 클릭 시
            btnSignupComplete.setOnClickListener {
                // 회원가입 정보를 담은 User 객체 생성
                val user = User(
                    id = etSignupId.text.toString(),
                    pw = etSignupPw.text.toString(),
                    nickname = etSignupNickname.text.toString(),
                    hobby = etSignupHobby.text.toString()
                )

                // 회원가입 형식이 맞으면 로그인 액티비티로 이동
                if (signupValid(user)) {
                    toast("회원가입 완료")
                    intentActivity(user)
                } else {
                    binding.root.snackbar("회원가입 형식과 맞지 않습니다.")
                }
            }
        }
    }

    private fun intentActivity(user: User) {
        // 회원가입 정보를 user data class에 담아 로그인 액티비티로 전달
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        intent.putExtra("user_data", user)
        setResult(RESULT_OK, intent)
        finish()
    }

    // 회원가입 유효성 확인
    private fun signupValid(user: User): Boolean {
        return user.id.length in 6..10 &&
                user.pw.length in 8..12 &&
                user.nickname.isNotBlank() && user.nickname.matches(nicknameRegex) &&
                user.hobby.isNotBlank() && user.hobby.matches(hobbyRegex)
    }

    //정규식 패턴
    companion object {
        private const val NICKNAME_PATTERN = "^[a-zA-Z0-9]*$"
        private const val HOBBY_PATTERN = "^[a-zA-Z0-9]*$"
        val nicknameRegex = Regex(NICKNAME_PATTERN)
        val hobbyRegex = Regex(HOBBY_PATTERN)
    }
}