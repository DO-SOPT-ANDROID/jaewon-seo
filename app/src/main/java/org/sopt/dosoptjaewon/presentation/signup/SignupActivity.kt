package org.sopt.dosoptjaewon.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sopt.common.context.hideKeyboard
import com.sopt.common.context.toast
import com.sopt.common.viewmodel.UniversalViewModelFactory
import org.sopt.dosoptjaewon.R.string
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.data.network.ServicePool.authService
import org.sopt.dosoptjaewon.data.network.repository.signup.SignupRepository
import org.sopt.dosoptjaewon.databinding.ActivitySignupBinding
import org.sopt.dosoptjaewon.presentation.login.LoginActivity

class SignupActivity : AppCompatActivity() {
    private val binding by lazy { ActivitySignupBinding.inflate(layoutInflater) }
    private val signupViewModel: SignupViewModel by viewModels {
        UniversalViewModelFactory {
            SignupViewModel(SignupRepository(authService))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.lifecycleOwner = this
        binding.signupViewModel = signupViewModel

        setupViewListeners()
        setupSignupStateObserver()
    }

    private fun setupViewListeners() {
        binding.btnSignupComplete.setOnClickListener { createUserAndSignup() }
    }

    private fun createUserAndSignup() {
        signupViewModel.postSignup()
    }

    private fun setupSignupStateObserver() {
        signupViewModel.signupState.observe(this) { state ->
            when (state) {
                is SignupState.Success -> {
                    toast(getString(string.signup_success))
                    // navigateToLoginActivity(state.user)
                }

                is SignupState.Failure -> {
                    toast(state.message)
                }

                else -> {}
            }
        }
    }

    private fun navigateToLoginActivity(user: User) {
        val intent = createLoginIntentWithUser(user)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun createLoginIntentWithUser(user: User): Intent =
        Intent(this, LoginActivity::class.java).apply {
            putExtra(EXTRA_USER, user)
        }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        const val EXTRA_USER = "EXTRA_USER"
    }

}
