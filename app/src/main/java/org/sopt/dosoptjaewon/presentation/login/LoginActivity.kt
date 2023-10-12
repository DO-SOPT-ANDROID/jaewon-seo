package org.sopt.dosoptjaewon.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityLoginBinding
import org.sopt.dosoptjaewon.presentation.main.MainActivity
import org.sopt.dosoptjaewon.presentation.signup.SignupActivity
import org.sopt.dosoptjaewon.util.snackbar
import org.sopt.dosoptjaewon.util.toast

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel = LoginViewModel()
    private val signupResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val user: User? = it.data?.getParcelableExtra("user_data")
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

        setOnClickListener()
    }

    // LoginActivity
    private fun setOnClickListener() {
        with(binding) {
            btnLoginSignin.setOnClickListener {
                if (loginValid()) {
                    toast("로그인 성공")
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra(EXTRA_DATA, viewModel.userInfo.value)
                    startActivity(intent)
                    finish()
                } else {
                    binding.root.snackbar("아이디 혹은 비밀번호를 다시 확인해주세요.")
                }
            }
            btnLoginSignup.setOnClickListener {
                val intent = Intent(this@LoginActivity, SignupActivity::class.java)
                signupResultLauncher.launch(intent)
            }
        }
    }

    private fun loginValid(): Boolean {
        val id = viewModel.userInfo.value?.id
        val pw = viewModel.userInfo.value?.pw

        return binding.etLoginId.text.toString() == id && binding.etLoginPw.text.toString() == pw
    }

    companion object {
        const val EXTRA_DATA = "user_data"
    }
}