package org.sopt.dosoptjaewon.presentation.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.sopt.common.context.toast
import com.sopt.common.view.snackBar
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityMainBinding
import org.sopt.dosoptjaewon.presentation.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()
    private var backPressedTime: Long = 0L
    private val backPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (System.currentTimeMillis() - backPressedTime < BACK_PRESS_INTERVAL) {
                isEnabled = false
                onBackPressedDispatcher.onBackPressed()
                finishAffinity() // 앱의 모든 액티비티를 종료
                System.exit(0)  // 앱 프로세스 종료
            } else {
                backPressedTime = System.currentTimeMillis()
                toast(getString(R.string.main_back_press))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getUserInfo()
        initView()
    }

    private fun getUserInfo() {
        val sharedPref = getSharedPreferences(PREF_KEY_USER_ID, MODE_PRIVATE)

        with(sharedPref) {
            val userId = getString(PREF_KEY_USER_ID, null)
            val userPw = getString(PREF_KEY_USER_PW, null)
            val userNickname = getString(PREF_KEY_USER_NICKNAME, null)
            val userHobby = getString(PREF_KEY_USER_HOBBY, null)

            if (listOf(userId, userPw, userNickname, userHobby).all { it != null }) {
                val user = User(
                    id = userId!!,
                    pw = userPw!!,
                    nickname = userNickname!!,
                    hobby = userHobby!!
                )
                viewModel.setUserInfo(user)
                binding.root.snackBar(getString(R.string.main_load_info_sucess))
            } else {
                binding.root.snackBar(getString(R.string.main_load_info_fail))
            }
        }
    }

    private fun initView() {
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        with(binding) {
            ivMainProfile.load(R.drawable.ic_profile)
            tvMainNickname.text = viewModel.userInfo.value?.nickname
            tvMainIdValue.text = viewModel.userInfo.value?.id
            tvMainHobbyValue.text = viewModel.userInfo.value?.hobby
        }

        binding.btnMainLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        clearUserPreference()
        Intent(this@MainActivity, LoginActivity::class.java).apply {
            // 로그아웃 후에 백버튼을 눌러도 다시 돌아가지 않도록 설정
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
            finish()
        }
    }

    private fun clearUserPreference() {
        val sharedPref = getSharedPreferences(PREF_KEY_USER_ID, MODE_PRIVATE)
        with(sharedPref.edit()) {
            remove(PREF_KEY_USER_ID)
            remove(PREF_KEY_USER_PW)
            remove(PREF_KEY_USER_NICKNAME)
            remove(PREF_KEY_USER_HOBBY)
            apply()
        }
    }

    companion object {
        private const val BACK_PRESS_INTERVAL = 2000 // 2초

        private const val PREF_KEY_USER_ID = "user_id"
        private const val PREF_KEY_USER_PW = "user_pw"
        private const val PREF_KEY_USER_NICKNAME = "user_nickname"
        private const val PREF_KEY_USER_HOBBY = "user_hobby"
    }
}