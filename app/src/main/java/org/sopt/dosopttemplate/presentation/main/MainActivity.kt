package org.sopt.dosopttemplate.presentation.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import coil.load
import org.sopt.dosopttemplate.R
import org.sopt.dosopttemplate.data.model.User
import org.sopt.dosopttemplate.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    private val viewModel = MainViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        getUserInfo()
        initUI()
    }

    private fun getUserInfo() {
        val user: User? = intent.getParcelableExtra("user_data") // User 객체 받기
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
}