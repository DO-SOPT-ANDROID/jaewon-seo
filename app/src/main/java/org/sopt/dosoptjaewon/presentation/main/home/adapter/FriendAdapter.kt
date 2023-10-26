package org.sopt.dosoptjaewon.presentation.main.home.adapter

import android.view.LayoutInflater
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
        fun onBind(friendInfo: Friend) {
            binding.ivFriendProfile.load(friendInfo.profileImage) {
                transformations(CircleCropTransformation())
            }
            binding.tvFriendName.text = friendInfo.name

            // if today is brithDay
            if (brithDayConfirm(friendInfo.birthDay)) {
                with(binding) {
                    llFriendGift.visibility = ViewGroup.VISIBLE
                    tvFriendName.text = friendInfo.name + "\uD83C\uDF82"
                    tvFriendBrithDay.visibility = ViewGroup.VISIBLE
                    tvFriendBrithDay.text =
                        friendInfo.birthDay.format(DateTimeFormatter.ofPattern(BIRTHDAY_PATTERN))
                }
            } else {
                // if music is not empty
                if (friendInfo.music.isNotEmpty()) {
                    binding.llFriendMusic.visibility = ViewGroup.VISIBLE
                    binding.tvFriendMusic.text = friendInfo.music
                }
            }

            // update is true
            if (friendInfo.update) binding.vFriendNew.visibility = ViewGroup.VISIBLE
        }

        private fun brithDayConfirm(date: LocalDate): Boolean {
            val today = LocalDate.now()
            return today.monthValue == date.monthValue && today.dayOfMonth == date.dayOfMonth
        }
    }

    companion object {
        private const val BIRTHDAY_PATTERN = "오늘 MM월 dd일"
    }

}
