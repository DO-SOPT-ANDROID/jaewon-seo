package org.sopt.dosoptjaewon.presentation.main.reqres.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.sopt.common.adapter.ItemDiffCallback
import org.sopt.dosoptjaewon.databinding.ItemFollwerBinding
import org.sopt.dosoptjaewon.domain.model.Follower

class FollowerAdapter : ListAdapter<Follower, FollowerAdapter.FollowerViewHolder>(
    ItemDiffCallback<Follower>(
        onItemsTheSame = { old, new -> old.email == new.email },
        onContentsTheSame = { old, new -> old == new }
    )
) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FollowerViewHolder {
        val binding = ItemFollwerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return FollowerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FollowerViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class FollowerViewHolder(private val binding: ItemFollwerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(follower: Follower) {
            with(binding) {
                ivFollowerAvartar.load(follower.avatar)
                tvFollowerName.text = follower.fullName
                tvFollowerEmail.text = follower.email
            }
        }
    }
}