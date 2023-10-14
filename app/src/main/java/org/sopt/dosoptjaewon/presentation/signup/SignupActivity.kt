package org.sopt.dosoptjaewon.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
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
    private val viewModel: SignupViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
        observeViewModel()
    }

    private fun initView() {
        with(binding) {
            btnSignupComplete.setOnClickListener {
                val user = User(
                    id = binding.etSignupId.text.toString(),
                    pw = binding.etSignupPw.text.toString(),
                    nickname = binding.etSignupNickname.text.toString(),
                    hobby = binding.etSignupHobby.text.toString()
                )
                viewModel.handleSignup(user)
            }
        }
    }

    private fun observeViewModel() {
        viewModel.signupState.observe(this) { state ->
            when (state) {
                is SignupState.Success -> {
                    toast(stringOf(string.signup_success))
                    navigateToLogin(state.user)
                }

                is SignupState.Failure -> {
                    binding.root.snackBar(getString(string.signup_fail))
                }

                else -> {}
            }
        }
    }

    private fun navigateToLogin(user: User) {
        // 회원가입 정보를 user data class에 담아 로그인 액티비티로 전달
        val intent = Intent(this@SignupActivity, LoginActivity::class.java)
        intent.putExtra(EXTRA_DATA, user)
        setResult(RESULT_OK, intent)
        finish()
    }

    // 키보드 숨기기
    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val EXTRA_DATA = "user_data"
    }
}