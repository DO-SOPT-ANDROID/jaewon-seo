package org.sopt.dosoptjaewon.presentation.signup

import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sopt.common.context.hideKeyboard
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
        setupViewListeners()
        setupViewModelObservers()
    }

    private fun setupViewListeners() {
        binding.btnSignupComplete.setOnClickListener { createUserAndSignup() }
    }

    private fun createUserAndSignup() {
        val user = createUserFromInput()
        viewModel.handleSignup(user)
    }

    private fun createUserFromInput(): User {
        return User(
            id = binding.etSignupId.text.toString(),
            pw = binding.etSignupPw.text.toString(),
            nickname = binding.etSignupNickname.text.toString(),
            hobby = binding.etSignupHobby.text.toString()
        )
    }

    private fun setupViewModelObservers() {
        viewModel.signupState.observe(this) { state ->
            when (state) {
                is SignupState.Success -> {
                    toast(getString(string.signup_success))
                    navigateToLoginActivity(state.user)
                }
                is SignupState.Failure -> {
                    binding.root.snackBar(string.signup_fail)
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
