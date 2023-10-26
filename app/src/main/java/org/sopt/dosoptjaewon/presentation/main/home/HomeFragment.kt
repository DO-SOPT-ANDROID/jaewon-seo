package org.sopt.dosoptjaewon.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.FragmentHomeBinding
import org.sopt.dosoptjaewon.presentation.main.MainActivity
import org.sopt.dosoptjaewon.presentation.main.home.adapter.FriendAdapter
import org.sopt.dosoptjaewon.presentation.main.home.adapter.UserAdapter


class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding) { "_binding is  null" }

    private val viewModel: HomeViewModel by lazy { HomeViewModel() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initAdapter()
        viewModel.setUserInfo(getBundleData())
    }

    private fun getBundleData(): User? {
        return arguments?.getParcelable<User>(MainActivity.USER_BUNDLE_KEY)
    }

    private fun initAdapter() {
        val userAdapter = UserAdapter(requireContext())
        val friendAdapter = FriendAdapter()
        val concatAdapter = ConcatAdapter(userAdapter, friendAdapter)
        binding.rvHome.layoutManager = LinearLayoutManager(context)
        binding.rvHome.adapter = concatAdapter

        viewModel.userInfo.observe(viewLifecycleOwner) {
            userAdapter.setUser(it)
        }

        viewModel.mockFriendsInfo.observe(viewLifecycleOwner) {
            friendAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}