package org.sopt.dosoptjaewon.presentation.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.databinding.FragmentHomeBinding
import org.sopt.dosoptjaewon.model.Friend
import org.sopt.dosoptjaewon.presentation.main.MainViewModel
import org.sopt.dosoptjaewon.presentation.main.home.adapter.FriendAdapter
import org.sopt.dosoptjaewon.presentation.main.home.adapter.UserAdapter
import java.time.LocalDate


class HomeFragment : Fragment(), AddFriendDialogFragment.AddFriendDialogListener {
    private var _binding: FragmentHomeBinding? = null
    private val binding: FragmentHomeBinding
        get() = requireNotNull(_binding) { "_binding is null" }

    private val mainViewModel: MainViewModel by activityViewModels()
    private lateinit var friendAdapter: FriendAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        friendAdapter = FriendAdapter()
        setupRecyclerView()
        setupFab()
        observeHomeViewModel()
    }

    private fun setupRecyclerView() {
        binding.rvHome.layoutManager = LinearLayoutManager(context)
        binding.rvHome.adapter = friendAdapter
    }

    private fun setupFab() {
        binding.fabHome.setOnClickListener {
            showAddFriendDialog()
        }
    }

    private fun showAddFriendDialog() {
        val dialog = AddFriendDialogFragment()
        dialog.setAddFriendDialogListener(this)
        dialog.show(parentFragmentManager, "AddFriendDialogFragment")
    }

    override fun onDialogPositiveClick(name: String, dob: LocalDate) {
        val newFriend = Friend(R.drawable.ic_profile, name, dob)
        friendAdapter.submitList(friendAdapter.currentList + newFriend)
    }

    private fun observeHomeViewModel() {
        mainViewModel.userInfo.observe(viewLifecycleOwner) {
            val userAdapter = UserAdapter(requireContext())
            userAdapter.setUser(it)
            binding.rvHome.adapter = ConcatAdapter(userAdapter, friendAdapter)
        }

        mainViewModel.mockFriendsInfo.observe(viewLifecycleOwner) {
            friendAdapter.submitList(it)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    fun scrollToTop() {
        binding.rvHome.scrollToPosition(0)
    }
}
