package com.example.fcmexample

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.fcmexample.databinding.LayoutNotificationBinding
import com.example.fcmexample.db.Notification

class NotificationListAdapter (
private val clickListener: ItemClickListener) :
ListAdapter<Notification, NotificationListAdapter.NotificationViewHolder>(NotificationDiffCallback) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position), clickListener)
    }

    class NotificationViewHolder private constructor(val binding: LayoutNotificationBinding) :
        RecyclerView.ViewHolder(binding.root) {
        //These items must be specified as variables in the xml item layout file
        fun bind(
            item: Notification,
            clickListener: ItemClickListener
        ) {
            binding.apply {
                notification = item
                this.clickListener = clickListener
                executePendingBindings()
            }
        }

        companion object {
            fun from(parent: ViewGroup): NotificationViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = LayoutNotificationBinding.inflate(layoutInflater, parent, false)
                return NotificationViewHolder(binding)
            }
        }
    }

    object NotificationDiffCallback : DiffUtil.ItemCallback<Notification>() {
        override fun areItemsTheSame(oldItem: Notification, newItem: Notification) = oldItem == newItem

        override fun areContentsTheSame(oldItem: Notification, newItem: Notification) = oldItem.content == newItem.content
    }
}

class ItemClickListener(val clickListener: (item: Notification) -> Unit) {
    fun onClick(item: Notification) = clickListener(item)
}