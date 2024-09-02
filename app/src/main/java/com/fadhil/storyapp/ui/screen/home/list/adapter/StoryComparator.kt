package com.fadhil.storyapp.ui.screen.home.list.adapter

import androidx.recyclerview.widget.DiffUtil
import com.fadhil.storyapp.domain.model.Story

object StoryComparator : DiffUtil.ItemCallback<Story>() {
    override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
        // Id is unique.
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
        return oldItem == newItem
    }
}