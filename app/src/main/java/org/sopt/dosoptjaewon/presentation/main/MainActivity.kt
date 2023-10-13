package org.sopt.dosoptjaewon.presentation.main

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import coil.load
import com.sopt.common.context.hideKeyboard
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getUserInfo()
        initUI()
    }

    private fun getUserInfo() {
        val user: User? = intent.getParcelableExtra(EXTRA_DATA) // User 객체 받기
        if (user != null) {
            viewModel.setUserInfo(user) // ViewModel에 User 객체 적용
        }
    }

    private fun initUI() {
        with(binding) {
            ivMainProfile.load(R.drawable.ic_profile)
            tvMainNickname.text = viewModel.userInfo.value?.nickname
            tvMainIdValue.text = viewModel.userInfo.value?.id
            tvMainHobbyValue.text = viewModel.userInfo.value?.hobby
        }
    }

    companion object {
        const val EXTRA_DATA = "user_data"
    }
}