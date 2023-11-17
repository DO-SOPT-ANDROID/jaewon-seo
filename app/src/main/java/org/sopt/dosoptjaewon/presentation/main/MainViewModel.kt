package org.sopt.dosoptjaewon.presentation.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.data.network.repository.main.MainRepository
import org.sopt.dosoptjaewon.domain.model.Friend
import java.time.LocalDate

class MainViewModel(private val mainRepository: MainRepository) : ViewModel() {
    private val _userInfo = MutableLiveData<User?>()
    val userInfo: MutableLiveData<User?>
        get() = _userInfo

    private val _friendsList = MutableLiveData<List<Friend>>()
    val friendsList: MutableLiveData<List<Friend>>
        get() = _friendsList

    val mockFriendsInfo = MutableLiveData(
        listOf(
            Friend(
                R.drawable.ic_profile1,
                "이태희",
                LocalDate.of(1999, 10, 28),
                "후라이의 꿈 - 이수현",
                true
            ),
            Friend(
                R.drawable.ic_profile3,
                "손명지",
                LocalDate.of(2001, 10, 28),
                "Home - 박효신"
            ),
            Friend(
                R.drawable.ic_profile2,
                "배찬우",
                LocalDate.of(2001, 10, 27),
                "",
                true
            ),
            Friend(
                R.drawable.ic_profile3,
                "최민영",
                LocalDate.of(2001, 1, 20),
                "Somebody - 디오 (D.O.)"
            ),
            Friend(
                R.drawable.ic_profile1,
                "김지영",
                LocalDate.of(1999, 11, 5),
            ),
            Friend(
                R.drawable.ic_profile2,
                "박소현",
                LocalDate.of(2001, 12, 7),
                "숲 - 최유리"
            ),
            Friend(
                R.drawable.ic_profile3,
                "배지현",
                LocalDate.of(2001, 11, 25)
            ),
            Friend(
                R.drawable.ic_profile1,
                "곽의진",
                LocalDate.of(2001, 7, 28),
                "Steal The Show(From \"엘리멘탈\") - Lauv",
                true
            ),
            Friend(
                R.drawable.ic_profile2,
                "최준서",
                LocalDate.of(1999, 5, 11),
                "Vancouver 2 - BIG Naughty (서동현)"
            ),
            Friend(
                R.drawable.ic_profile1,
                "박강희",
                LocalDate.of(1999, 10, 27),
                "HAPPY - NF"
            ),
            Friend(
                R.drawable.ic_profile2,
                "김상호",
                LocalDate.of(2000, 3, 27),
                "Dancingintherain' (feat.Minggunyu) - 곽태풍"
            ),
            Friend(
                R.drawable.ic_profile3,
                "우상욱",
                LocalDate.of(1998, 9, 26),
                "Dancingintherain' (feat.Minggunyu) - 곽태풍"
            )
        )
    )

    init {
        setFriendsList(mockFriendsInfo.value!!)
    }

    private fun setFriendsList(friends: List<Friend>) {
        _friendsList.value = friends
    }

    fun getUserInfo(memberId: Int) {
        viewModelScope.launch {
            runCatching {
                mainRepository.getUserInfo(memberId)
            }.onSuccess {
                _userInfo.value = User(it.id, "", it.nickname, "")
            }.onFailure {
                Log.d("MainViewModel", "getUserInfoError: ${it.message}")
            }
        }
    }
}
