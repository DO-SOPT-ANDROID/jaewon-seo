package org.sopt.dosoptjaewon.presentation.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sopt.common.context.toast
import com.sopt.common.view.snackBar
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityMainBinding
import org.sopt.dosoptjaewon.presentation.login.LoginActivity.Companion.PREF_FILE_USER
import org.sopt.dosoptjaewon.presentation.main.doandroid.DoAndroidFragment
import org.sopt.dosoptjaewon.presentation.main.home.HomeFragment
import org.sopt.dosoptjaewon.presentation.main.mypage.MypageFragment
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val mainViewModel: MainViewModel by viewModels()
    private var backPressedTime: Long = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupViewModel()
        setupNavigation()
        setupBackPressedHandler()

        if (savedInstanceState == null) {
            setupDefaultFragment()
            setupDefaultBottomNavigation()
        }
    }

    private fun setupViewModel() {
        val user = getUserInfo()
        if (user != null) {
            mainViewModel.setUserInfo(user)
        } else {
            showUserInfoLoadError()
        }
    }

    private fun showUserInfoLoadError() {
        binding.root.snackBar(R.string.mypage_load_info_error)
    }

    private fun setupNavigation() {
        setupNavigationBar()
        setupBottomNavigationClickListener()
    }

    private fun setupNavigationBar() {
        binding.bnvMain.setSelectedItemId(R.id.menu_main_home)
    }

    private fun setupBottomNavigationClickListener() {
        binding.bnvMain.setOnItemSelectedListener { item ->
            val fragment = getFragmentForMenuItem(item.itemId)
            if (fragment != null) {
                replaceFragment(fragment)
                true
            } else {
                false
            }
        }

        binding.bnvMain.setOnItemReselectedListener { item ->
            when (item.itemId) {
                R.id.menu_main_home -> {
                    (supportFragmentManager.findFragmentById(R.id.fcv_main) as? HomeFragment)?.scrollToTop()
                }
            }
        }
    }

    private fun getFragmentForMenuItem(itemId: Int): Fragment? {
        return when (itemId) {
            R.id.menu_main_do_android -> DoAndroidFragment()
            R.id.menu_main_home -> HomeFragment()
            R.id.menu_main_mypage -> MypageFragment()
            else -> null
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_main, fragment)
            .commit()
    }

    private fun setupDefaultFragment() {
        replaceFragment(HomeFragment())
    }

    private fun setupDefaultBottomNavigation() {
        binding.bnvMain.selectedItemId = R.id.menu_main_home
    }

    private fun setupBackPressedHandler() {
        val backPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (isBackPressIntervalElapsed()) {
                    exitApplication()
                } else {
                    updateBackPressedTime()
                    toast(getString(R.string.main_back_press))
                }
            }
        }
        onBackPressedDispatcher.addCallback(this, backPressedCallback)
    }

    private fun isBackPressIntervalElapsed() =
        System.currentTimeMillis() - backPressedTime < BACK_PRESS_INTERVAL

    private fun updateBackPressedTime() {
        backPressedTime = System.currentTimeMillis()
    }

    private fun exitApplication() {
        finishAffinity()
        exitProcess(0)
    }

    private fun getUserInfo(): User? {
        val sharedPref = getSharedPreferences(PREF_FILE_USER, MODE_PRIVATE)
        return with(sharedPref) {
            val userId = getString(PREF_KEY_USER_ID, null)
            val userPw = getString(PREF_KEY_USER_PW, null)
            val userNickname = getString(PREF_KEY_USER_NICKNAME, null)
            val userHobby = getString(PREF_KEY_USER_HOBBY, null)
            if (userId != null && userPw != null && userNickname != null && userHobby != null) {
                User(userId, userPw, userNickname, userHobby)
            } else {
                null
            }
        }
    }

    companion object {
        private const val BACK_PRESS_INTERVAL = 2000 // 2초
        const val PREF_KEY_USER_ID = "PREF_KEY_USER_ID"
        const val PREF_KEY_USER_PW = "PREF_KEY_USER_PW"
        const val PREF_KEY_USER_NICKNAME = "PREF_KEY_USER_NICKNAME"
        const val PREF_KEY_USER_HOBBY = "PREF_KEY_USER_HOBBY"
    }
}