package org.sopt.dosopttemplate.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.databinding.ActivityLoginBinding
import org.sopt.dosopttemplate.presentation.main.MainActivity
import org.sopt.dosopttemplate.presentation.signup.SignupActivity
import org.sopt.dosopttemplate.util.snackbar

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel = LoginViewModel()
    private val signupResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                fetchData()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setOnClickListener()
    }

    private fun fetchData() {
        val receivedId = intent.getStringExtra("id").toString()
        val receivedPw = intent.getStringExtra("pw").toString()
        val receivedNickname = intent.getStringExtra("nickname").toString()
        val receivedHobby = intent.getStringExtra("hobby").toString()

        if (!intent.getStringExtra("id").isNullOrBlank()) {
            binding.etLoginId.setText(receivedId)
        }

        viewModel.setUserInfo(receivedId, receivedPw, receivedNickname, receivedHobby)
    }

    private fun setOnClickListener() {
        with(binding) {
            btnLoginSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                signupResultLauncher.launch(intent)
            }
            btnLoginSignin.setOnClickListener {
                checkLoginInfo()
            }
        }
    }

    private fun checkLoginInfo() {
        val id = viewModel.userInfo.value?.id
        val pw = viewModel.userInfo.value?.pw

        with(binding) {
            if (etLoginId.text.toString() == id && etLoginPw.text.toString() == pw) {
                snackbar("로그인 성공")
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                snackbar("아이디와 비밀번호를 확인해주세요")
            }
        }
    }
}