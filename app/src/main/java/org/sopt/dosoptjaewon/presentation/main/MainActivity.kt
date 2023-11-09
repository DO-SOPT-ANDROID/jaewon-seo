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
                toast(getString(R.string.main_back_press))
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        clickBottomNavigation()
        // 기본 프래그먼트로 HomeFragment를 설정합니다.
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fcv_main, HomeFragment())
                .commit()
        }
        initView()
    }

    private fun initView() {
        // 하단 네비게이션 바의 기본 선택을 설정합니다.
        binding.bnvMain.setSelectedItemId(R.id.menu_main_home)
        binding.bnvMain.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.menu_main_home -> {
                    (supportFragmentManager.findFragmentById(R.id.fcv_main) as? HomeFragment)?.scrollToTop()
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun clickBottomNavigation() {
        binding.bnvMain.setOnItemSelectedListener {
            // User 정보를 넘겨줄 bundle 생성
            val bundle = Bundle().apply {
                putParcelable(USER_BUNDLE_KEY, getUserInfo())
            }
            when (it.itemId) {
                R.id.menu_main_do_android -> {
                    replaceFragment(DoAndroidFragment())
                    true
                }

                R.id.menu_main_home -> {
                    val fragment = HomeFragment().apply {
                        arguments = bundle
                    }
                    replaceFragment(fragment)
                    true
                }

                R.id.menu_main_mypage -> {
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
                return User(
                    id = userId!!,
                    pw = userPw!!,
                    nickname = userNickname!!,
                    hobby = userHobby!!
                )
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