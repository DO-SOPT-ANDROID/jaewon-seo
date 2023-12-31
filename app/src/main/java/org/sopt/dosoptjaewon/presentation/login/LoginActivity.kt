package org.sopt.dosoptjaewon.presentation.login

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.sopt.common.context.hideKeyboard
import com.sopt.common.context.toast
import com.sopt.common.viewmodel.UniversalViewModelFactory
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.network.ServicePool.authService
import org.sopt.dosoptjaewon.data.network.repository.login.LoginRepository
import org.sopt.dosoptjaewon.databinding.ActivityLoginBinding
import org.sopt.dosoptjaewon.presentation.main.MainActivity
import org.sopt.dosoptjaewon.presentation.signup.SignupActivity
import org.sopt.dosoptjaewon.presentation.signup.SignupState

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val loginViewModel: LoginViewModel by viewModels {
        UniversalViewModelFactory {
            LoginViewModel(LoginRepository(authService))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setupViewListeners()
        setupLoginStateObserve()
    }

    override fun onStart() {
        super.onStart()
        attemptAutoLogin()
    }

    private fun attemptAutoLogin() {
        getSharedPreferences(PREF_FILE_USER, MODE_PRIVATE).run {
            val userId = getString(PREF_KEY_USER_ID, null)
            val userPw = getString(PREF_KEY_USER_PW, null)

            if (userId != null && userPw != null) {
                loginViewModel.handleLogin(userId, userPw)
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
            loginViewModel.handleLogin(id, pw)
        }
    }

    private fun setupLoginStateObserve() {
        loginViewModel.loginState
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is LoginState.Success -> {
                        toast(getString(R.string.login_welcome_user).format(state.server_id))
                        if (binding.cbLoginAutoLogin.isChecked) saveUserToPreferences()
                        navigateToMainActivity(state.server_id)
                    }

                    is LoginState.Failure -> {
                        toast(getString(R.string.login_fail))
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }

    private fun navigateToMainActivity(serverUserId: Int) {
        Intent(this, MainActivity::class.java).also { intent ->
            intent.putExtra(EXTRA_MEMBER_ID, serverUserId)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
    }

    private fun saveUserToPreferences() {
        val id = binding.etLoginId.text.toString()
        val pw = binding.etLoginPw.text.toString()

        getSharedPreferences(PREF_FILE_USER, MODE_PRIVATE).edit().apply {
            putString(PREF_KEY_USER_ID, id)
            putString(PREF_KEY_USER_PW, pw)
            apply()
        }
    }

    private fun navigateToSignup() {
        Intent(this, SignupActivity::class.java).also { intent ->
            startActivity(intent)
        }
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val EXTRA_USER = "EXTRA_USER"
        private const val EXTRA_MEMBER_ID = "EXTRA_MEMBER_ID"

        const val PREF_FILE_USER = "USER_PREFERENCES"
        const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
        const val PREF_KEY_USER_PW = "PREF_KEY_USER_PW"
    }
}
