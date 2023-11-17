package org.sopt.dosoptjaewon.presentation.main

import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.sopt.common.context.toast
import com.sopt.common.view.snackBar
import com.sopt.common.viewmodel.UniversalViewModelFactory
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.network.ServicePool.authService
import org.sopt.dosoptjaewon.data.network.repository.main.MainRepository
import org.sopt.dosoptjaewon.databinding.ActivityMainBinding
import org.sopt.dosoptjaewon.presentation.main.doandroid.DoAndroidFragment
import org.sopt.dosoptjaewon.presentation.main.home.HomeFragment
import org.sopt.dosoptjaewon.presentation.main.mypage.MypageFragment
import kotlin.system.exitProcess


class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private var backPressedTime: Long = 0L

    private val mainViewModel: MainViewModel by viewModels {
        UniversalViewModelFactory {
            MainViewModel(MainRepository(authService))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        setupNavigation()
        setupBackPressedHandler()

        if (savedInstanceState == null) {
            setupDefaultFragment()
            setupDefaultBottomNavigation()
            setupUerInfo()
        }
    }

    private fun setupUerInfo() {
        mainViewModel.getUserInfo(intent.getIntExtra(EXTRA_MEMBER_ID, 0))
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

    companion object {
        private const val BACK_PRESS_INTERVAL = 2000 // 2초
        private const val EXTRA_MEMBER_ID = "EXTRA_MEMBER_ID"
    }
}