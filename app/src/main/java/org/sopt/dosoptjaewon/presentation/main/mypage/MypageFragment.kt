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
import org.sopt.dosoptjaewon.presentation.login.LoginActivity.Companion.PREF_FILE_USER
import org.sopt.dosoptjaewon.presentation.main.MainActivity

class MypageFragment : Fragment() {
    private var _binding: FragmentMypageBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMypageBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        extractUserFromArguments()?.let { user ->
            displayUserInfo(user)
            setupLogoutButton()
        }
    }

    private fun extractUserFromArguments(): User? {
        return arguments?.getParcelable(MainActivity.USER_BUNDLE_KEY)
    }

    private fun displayUserInfo(user: User) {
        binding.apply {
            tvMypageNickname.text = user.nickname
            tvMypageIdValue.text = user.id
            tvMypageHobbyValue.text = user.hobby
            ivMypageProfile.load(R.drawable.ic_profile)
        }
    }

    private fun setupLogoutButton() {
        binding.btnMypageLogout.setOnClickListener { logout() }
    }

    private fun logout() {
        clearUserPreferences()
        navigateToLoginActivity()
    }

    private fun navigateToLoginActivity() {
        startActivity(Intent(requireContext(), LoginActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        })
    }

    private fun clearUserPreferences() {
        requireActivity().getSharedPreferences(PREF_FILE_USER, AppCompatActivity.MODE_PRIVATE)
            .edit().apply {
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
