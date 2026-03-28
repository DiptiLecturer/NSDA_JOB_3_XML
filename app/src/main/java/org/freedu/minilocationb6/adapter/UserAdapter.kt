package org.freedu.minilocationb6.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.databinding.ItemUserBinding

class UserAdapter(
    private val onItemClick: (AppUsers) -> Unit
) : ListAdapter<AppUsers, UserAdapter.UserViewHolder>(DIFF_CALLBACK) {

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<AppUsers>() {
            override fun areItemsTheSame(oldItem: AppUsers, newItem: AppUsers) =
                oldItem.userId == newItem.userId

            override fun areContentsTheSame(oldItem: AppUsers, newItem: AppUsers) =
                oldItem == newItem
        }
    }

    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return UserViewHolder(binding)  // ← Just this line!
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.binding.tvUsername.text = user.username.ifEmpty { "No Name" }
        holder.binding.tvEmail.text = user.email
        holder.binding.tvLat.text = "Lat: ${user.latitude ?: "N/A"}"
        holder.binding.tvLng.text = "Lng: ${user.longitude ?: "N/A"}"

        // Set click listener here with guaranteed correct position
        holder.itemView.setOnClickListener {
            onItemClick(user)  // Use the user object directly - most reliable!
        }
    }


}