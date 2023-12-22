package org.sopt.dosoptjaewon.presentation.main.reqres

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.sopt.common.context.toast
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.sopt.dosoptjaewon.databinding.FragmentReqresBinding
import org.sopt.dosoptjaewon.presentation.main.reqres.adapter.FollowerAdapter

class ReqresFragment : Fragment() {

    private var _binding: FragmentReqresBinding? = null
    private val binding: FragmentReqresBinding
        get() = requireNotNull(_binding) { "_binding is  null" }
    private val reqresViewModel: ReqresViewModel by viewModels()
    private lateinit var followerAdapter: FollowerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentReqresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        followerAdapter = FollowerAdapter()
        setupRecyclerView()
        setupReqresStateObserver()
    }

    private fun setupRecyclerView() {
        binding.rvFollower.layoutManager = GridLayoutManager(context, 2)
        binding.rvFollower.adapter = followerAdapter
    }

    private fun setupReqresStateObserver() {
        reqresViewModel.reqresState
            .flowWithLifecycle(lifecycle)
            .onEach { state ->
                when (state) {
                    is ReqresState.Success -> {
                        reqresViewModel.followers.value?.let { followerAdapter.submitList(it) }
                    }

                    is ReqresState.Failure -> {
                        context?.toast(state.message)
                    }

                    else -> {}
                }
            }.launchIn(lifecycleScope)
    }
}