package org.sopt.dosoptjaewon.presentation.login

import android.content.Intent
import android.content.SharedPreferences
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
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val user: User? = it.data?.getParcelableExtra(EXTRA_DATA)
                if (user != null) {
                    // 로그인화면에 아이디는 자동으로 작성되게
                    binding.etLoginId.setText(user.id)
                    viewModel.setUserInfo(user)
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    override fun onStart() {
        super.onStart()
        checkAutoLogin()
    }

    private fun initView() {
        with(binding) {
            btnLoginSignin.setOnClickListener { handleLoginClick() }
            btnLoginSignup.setOnClickListener { handleSignupClick() }
        }
    }

    private fun checkAutoLogin() {
        val sharedPref = getSharedPreferences(PREF_KEY_USER_ID, MODE_PRIVATE)
        val userId = sharedPref.getString(PREF_KEY_USER_ID, null)
        val userPw = sharedPref.getString(PREF_KEY_USER_PW, null)

        if (!userId.isNullOrEmpty() && !userPw.isNullOrEmpty()) {
            navigateToMypage()
        }
    }

    private fun navigateToMypage() {
        Intent(this@LoginActivity, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

    private fun handleLoginClick() {
        val id = binding.etLoginId.text.toString()
        val pw = binding.etLoginPw.text.toString()

        if (viewModel.loginValid(id,pw)) {
            binding.root.snackBar(getString(R.string.login_success))
            navigateToMain()
        } else {
            binding.root.snackBar(getString(R.string.login_fail))
        }
    }

    private fun navigateToMain() {
        Intent(this@LoginActivity, MainActivity::class.java).apply {
            putExtra(EXTRA_DATA, viewModel.userInfo.value)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            saveUserPreference() // 사용자 정보 저장
            startActivity(this)
            finish()
        }
    }

    private fun saveUserPreference() {
        val sharedPref = getSharedPreferences(PREF_KEY_USER_ID, MODE_PRIVATE)
        sharedPref.edit().apply {
            viewModel.userInfo.value?.saveTo(this)
            apply()
        }
    }

    private fun User.saveTo(editor: SharedPreferences.Editor) {
        editor.putString(PREF_KEY_USER_ID, id)
        editor.putString(PREF_KEY_USER_PW, pw)
        editor.putString(PREF_KEY_USER_NICKNAME, nickname)
        editor.putString(PREF_KEY_USER_HOBBY, hobby)
    }

    private fun handleSignupClick() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        signupResultLauncher.launch(intent)
    }



    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        hideKeyboard(currentFocus ?: View(this))
        return super.dispatchTouchEvent(ev)
    }

    companion object {
        private const val EXTRA_DATA = "user_data"
        private const val PREF_KEY_USER_ID = "user_id"
        private const val PREF_KEY_USER_PW = "user_pw"
        private const val PREF_KEY_USER_NICKNAME = "user_nickname"
        private const val PREF_KEY_USER_HOBBY = "user_hobby"
    }
}