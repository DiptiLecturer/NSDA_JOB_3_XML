package org.freedu.minilocationb6.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import org.freedu.minilocationb6.Model.AppUsers
import org.freedu.minilocationb6.databinding.ItemUserBinding

class UserAdapter(
    private val userList: List<AppUsers>,
    private val onItemClick: (AppUsers) -> Unit
) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    inner class UserViewHolder(val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val binding = ItemUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.binding.tvUsername.text = user.username.ifEmpty { "No Name" }
        holder.binding.tvEmail.text = user.email
        holder.binding.tvLat.text = "Lat: ${user.latitude ?: "N/A"}"
        holder.binding.tvLng.text = "Long: ${user.longitude ?: "N/A"}"

        holder.itemView.setOnClickListener { onItemClick(user) }
    }

    override fun getItemCount(): Int = userList.size
}