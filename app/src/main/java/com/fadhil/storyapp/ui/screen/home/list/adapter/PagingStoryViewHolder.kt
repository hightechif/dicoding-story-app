package com.fadhil.storyapp.ui.screen.home.list.adapter

import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.fadhil.storyapp.databinding.ItemRowStoryBinding
import com.fadhil.storyapp.domain.model.Story

class PagingStoryViewHolder(private val binding: ItemRowStoryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(story: Story?, position: Int, delegate: StoryDelegate?) {
        story?.let {
            binding.tvTitle.text = story.name
            binding.tvDate.text = story.getCreatedDateDisplay()
            binding.tvDescription.text = story.description
            binding.root.setOnClickListener {
                delegate?.setOnClickListener(binding.root, story.id)
            }
            Glide.with(binding.root.context).load(story.photoUrl).into(binding.ivStoryIllustration)
        }
    }

}