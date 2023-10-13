package org.sopt.dosoptjaewon.presentation.login

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.sopt.common.context.toast
import com.sopt.common.view.snackBar
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityLoginBinding
import org.sopt.dosoptjaewon.presentation.main.MainActivity
import org.sopt.dosoptjaewon.presentation.signup.SignupActivity

class LoginActivity : AppCompatActivity() {

    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val viewModel: LoginViewModel by viewModels()
    private lateinit var signupResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initResultLauncher()
        initView()
    }

    private fun initResultLauncher() =
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

    private fun initView() {
        with(binding) {
            btnLoginSignin.setOnClickListener { handleLoginClick() }
            btnLoginSignup.setOnClickListener { handleSignupClick() }
        }
    }

    private fun handleLoginClick() {
        if (loginValid()) {
            toast("로그인 성공!")
            Intent(this@LoginActivity, MainActivity::class.java).apply {
                putExtra(EXTRA_DATA, viewModel.userInfo.value)
                startActivity(this)
                finish()
            }
        } else {
            binding.root.snackBar("아이디 혹은 비밀번호를 다시 확인해주세요.")
        }
    }

    private fun handleSignupClick() {
        val intent = Intent(this@LoginActivity, SignupActivity::class.java)
        signupResultLauncher.launch(intent)
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