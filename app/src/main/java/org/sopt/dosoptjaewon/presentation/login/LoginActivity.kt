package org.sopt.dosoptjaewon.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sopt.common.context.hideKeyboard
import com.sopt.common.view.snackBar
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityLoginBinding
import org.sopt.dosoptjaewon.presentation.main.MainActivity
import org.sopt.dosoptjaewon.presentation.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    private val signupResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                result.data?.getParcelableExtra<User>(EXTRA_USER)?.let { user ->
                    binding.etLoginId.setText(user.id)
                    viewModel.setUserInfo(user)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewListeners()
    }

    override fun onStart() {
        super.onStart()
        attemptAutoLogin()
    }

    private fun attemptAutoLogin() {
        getSharedPreferences(PREF_FILE_USER, MODE_PRIVATE).run {
            val userId = getString(PREF_KEY_USER_ID, null)
            val userPw = getString(PREF_KEY_USER_PW, null)
            if (!userId.isNullOrEmpty() && !userPw.isNullOrEmpty()) {
                navigateToMainActivity()
            }
        }
    }

    private fun setupViewListeners() {
        with(binding) {
            btnLoginSignin.setOnClickListener { attemptLogin() }
            btnLoginSignup.setOnClickListener { navigateToSignup() }
        }
    }

    private fun attemptLogin() {
        with(binding) {
            val id = etLoginId.text.toString()
            val pw = etLoginPw.text.toString()

            if (viewModel.isLoginValid(id, pw)) {
                onLoginSuccess(viewModel.userInfo.value)
            } else {
                root.snackBar(R.string.login_fail)
            }
        }
    }

    private fun onLoginSuccess(user: User?) {
        user?.let {
            saveUserToPreferences(it) // 사용자 정보를 저장
            navigateToMainActivity(it)
            binding.root.snackBar(R.string.login_success)
        }
    }


    private fun navigateToMainActivity(user: User? = null) {
        Intent(this, MainActivity::class.java).also { intent ->
            user?.let {
                saveUserToPreferences(it)
                intent.putExtra(EXTRA_USER, it)
            }
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }


    private fun saveUserToPreferences(user: User) {
        getSharedPreferences(PREF_FILE_USER, MODE_PRIVATE).edit().apply {
            putString(PREF_KEY_USER_ID, user.id)
            putString(PREF_KEY_USER_PW, user.pw)
            putString(PREF_KEY_USER_NICKNAME, user.nickname)
            putString(PREF_KEY_USER_HOBBY, user.hobby)
            apply()
        }
    }

    private fun navigateToSignup() {
        signupResultLauncher.launch(Intent(this, SignupActivity::class.java))
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val EXTRA_USER = "EXTRA_USER"

        const val PREF_FILE_USER = "USER_PREFERENCES"
        private const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
        private const val PREF_KEY_USER_PW = "PREF_KEY_USER_PW"
        private const val PREF_KEY_USER_NICKNAME = "PREF_KEY_USER_NICKNAME"
        private const val PREF_KEY_USER_HOBBY = "PREF_KEY_USER_HOBBY"
    }
}
