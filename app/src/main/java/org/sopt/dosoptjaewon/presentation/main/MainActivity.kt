package org.sopt.dosoptjaewon.presentation.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sopt.common.context.toast
import com.sopt.common.view.snackBar
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityMainBinding
import org.sopt.dosoptjaewon.presentation.main.doandroid.DoAndroidFragment
import org.sopt.dosoptjaewon.presentation.main.home.HomeFragment
import org.sopt.dosoptjaewon.presentation.main.mypage.MypageFragment


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
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
                toast(getString(R.string.mypage_back_press))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initView()
    }

    private fun initView() {
        onBackPressedDispatcher.addCallback(this, backPressedCallback)

        clickBottomNavigation()
    }

    private fun clickBottomNavigation() {
        binding.bnvMain.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.menu_main_do_android -> {
                    replaceFragment(DoAndroidFragment())
                    true
                }

                R.id.menu_main_home -> {
                    replaceFragment(HomeFragment())
                    true
                }

                R.id.menu_main_mypage -> {
                    // MypageFragment에 사용자 정보 전달
                    val bundle = Bundle().apply {
                        putParcelable(USER_BUNDLE_KEY, getUserInfo())
                    }

                    // Fragment에 argument 전달
                    val fragment = MypageFragment().apply {
                        arguments = bundle
                    }
                    replaceFragment(fragment)
                    true
                }

                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main, fragment)
            .commit()
    }

    private fun getUserInfo(): User? {
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
                binding.root.snackBar(getString(R.string.mypage_load_info_sucess))
                return user
            } else {
                binding.root.snackBar(getString(R.string.mypage_load_info_fail))
                return null
            }
        }
    }

    companion object {
        private const val BACK_PRESS_INTERVAL = 2000 // 2초

        const val PREF_KEY_USER_ID = "user_id"
        const val PREF_KEY_USER_PW = "user_pw"
        const val PREF_KEY_USER_NICKNAME = "user_nickname"
        const val PREF_KEY_USER_HOBBY = "user_hobby"

        const val USER_BUNDLE_KEY = "user_info"
    }
}