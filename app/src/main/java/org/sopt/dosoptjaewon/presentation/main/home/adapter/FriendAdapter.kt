package org.sopt.dosoptjaewon.presentation.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import com.sopt.common.adapter.ItemDiffCallback
import org.sopt.dosoptjaewon.databinding.ItemFriendBinding
import org.sopt.dosoptjaewon.model.Friend
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class FriendAdapter : ListAdapter<Friend, FriendAdapter.FriendViewHolder>(
    ItemDiffCallback<Friend>(
        onItemsTheSame = { old, new -> old.name == new.name },
        onContentsTheSame = { old, new -> old == new }
    )
) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FriendViewHolder {
        val binding = ItemFriendBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FriendViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FriendViewHolder, position: Int) {
        val friendInfo = getItem(position)
        holder.onBind(friendInfo)
    }

    class FriendViewHolder(private val binding: ItemFriendBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(friend: Friend) {
            displayProfileImage(friend.profileImage)
            displayFriendName(friend.name)
            displayBirthdayIfToday(friend)
            displayMusicIfNotEmpty(friend)
            displayUpdateIndicator(friend)
        }

        private fun displayProfileImage(imageResId: Int) {
            binding.ivFriendProfile.load(imageResId) {
                transformations(CircleCropTransformation())
            }
        }

        private fun displayFriendName(name: String) {
            binding.tvFriendName.text = name
        }

        private fun displayBirthdayIfToday(friend: Friend) {
            if (isTodayBirthday(friend.birthDay)) {
                binding.llFriendGift.visibility = View.VISIBLE
                binding.tvFriendName.text = friend.name + "\uD83C\uDF82"
                binding.tvFriendBrithDay.text = formatBirthday(friend.birthDay)
            } else {
                binding.llFriendGift.visibility = View.GONE
            }
        }

        private fun displayMusicIfNotEmpty(friend: Friend) {
            if (friend.music.isNotEmpty()) {
                binding.llFriendMusic.visibility = View.VISIBLE
                binding.tvFriendMusic.text = friend.music
            } else {
                binding.llFriendMusic.visibility = View.GONE
            }
        }

        private fun displayUpdateIndicator(friend: Friend) {
            binding.vFriendNew.visibility = if (friend.update) View.VISIBLE else View.GONE
        }

        private fun isTodayBirthday(date: LocalDate): Boolean {
            val today = LocalDate.now()
            return today.monthValue == date.monthValue && today.dayOfMonth == date.dayOfMonth
        }

        private fun formatBirthday(date: LocalDate): String =
            date.format(DateTimeFormatter.ofPattern(BIRTHDAY_PATTERN))
    }

    companion object {
        private const val BIRTHDAY_PATTERN = "오늘 MM월 dd일"
    }

}
