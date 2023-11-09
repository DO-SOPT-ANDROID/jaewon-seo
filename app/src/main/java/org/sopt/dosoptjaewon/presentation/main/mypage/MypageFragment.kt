package org.sopt.dosoptjaewon.presentation.main.mypage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import coil.load
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.FragmentMypageBinding
import org.sopt.dosoptjaewon.presentation.login.LoginActivity
import org.sopt.dosoptjaewon.presentation.main.MainActivity

class MypageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding: FragmentMypageBinding
        get() = requireNotNull(_binding) { "_binding is  null" }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Bundle에서 User 객체 가져와서 전달
        initView(getBundleData())
    }

    private fun getBundleData(): User? {
        return arguments?.getParcelable<User>(MainActivity.USER_BUNDLE_KEY)
    }

    private fun initView(user: User?) {
        // User 객체 사용하여 정보 표시
        user?.let {
            with(binding) {
                tvMypageNickname.text = it.nickname
                tvMypageIdValue.text = it.id
                tvMypageHobbyValue.text = it.hobby
                ivMypageProfile.load(R.drawable.ic_profile)
            }
        }

        binding.btnMypageLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        clearUserPreference()
        Intent(requireContext(), LoginActivity::class.java).apply {
            // 로그아웃 후에 백버튼을 눌러도 다시 돌아가지 않도록 설정
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(this)
        }
    }

    private fun clearUserPreference() {
        val sharedPref = requireActivity().getSharedPreferences(
            MainActivity.PREF_KEY_USER_ID,
            AppCompatActivity.MODE_PRIVATE
        )
        with(sharedPref.edit()) {
            remove(MainActivity.PREF_KEY_USER_ID)
            remove(MainActivity.PREF_KEY_USER_PW)
            remove(MainActivity.PREF_KEY_USER_NICKNAME)
            remove(MainActivity.PREF_KEY_USER_HOBBY)
            apply()
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}