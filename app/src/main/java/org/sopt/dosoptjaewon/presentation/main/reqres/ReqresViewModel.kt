package org.sopt.dosoptjaewon.presentation.main.reqres

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.sopt.dosoptjaewon.data.network.ServicePool.reqresService
import org.sopt.dosoptjaewon.data.network.model.main.reqres.ReqresResponse
import org.sopt.dosoptjaewon.domain.model.Follower

class ReqresViewModel : ViewModel() {
    private val _followers = MutableLiveData<List<Follower>>()
    val followers: MutableLiveData<List<Follower>>
        get() = _followers

    val reqresState = MutableStateFlow<ReqresState>(ReqresState.Idle)

    init {
        loadFollowers()
    }

    private fun loadFollowers() {
        viewModelScope.launch {
            runCatching {
                reqresService.getFollowerList()
            }.onSuccess { processSuccess(it) }
                .onFailure { processFailure(it) }
        }
    }

    private fun processSuccess(response: ReqresResponse) {
        _followers.value = response.toFollowerList()
        reqresState.value = ReqresState.Success
    }

    private fun processFailure(exception: Throwable) {
        reqresState.value = ReqresState.Failure("Failure: ${exception.message}")
    }
}