package org.sopt.dosoptjaewon.presentation.main.home.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.CircleCropTransformation
import org.sopt.dosoptjaewon.R
import org.sopt.dosoptjaewon.data.model.User
import org.sopt.dosoptjaewon.databinding.ItemUserBinding

class UserAdapter(context: Context) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {
    private val inflater by lazy { LayoutInflater.from(context) }
    private var userInfo: User? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        userInfo?.let { holder.onBind(it) }
    }

    override fun getItemCount() = if (userInfo != null) 1 else 0

    fun setUser(userInfo: User?) {
        this.userInfo = userInfo
        notifyDataSetChanged()
    }

    class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(userInfo: User) {
            binding.tvUserName.text = userInfo.nickname
            binding.ivUserProfile.load(R.drawable.ic_profile) {
                transformations(CircleCropTransformation())
            }
        }
    }
}
