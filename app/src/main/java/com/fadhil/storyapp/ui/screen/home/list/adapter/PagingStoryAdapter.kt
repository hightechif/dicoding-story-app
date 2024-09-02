package com.fadhil.storyapp.ui.screen.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import com.fadhil.storyapp.databinding.ItemRowStoryBinding
import com.fadhil.storyapp.domain.model.Story

class PagingStoryAdapter(diffCallback: DiffUtil.ItemCallback<Story>) :
    PagingDataAdapter<Story, PagingStoryViewHolder>(diffCallback) {

    private lateinit var binding: ItemRowStoryBinding
    var delegate: StoryDelegate? = null

    override fun onBindViewHolder(holder: PagingStoryViewHolder, position: Int) {
        val item = getItem(position)
        // Note that item can be null. ViewHolder must support binding a
        // null item as a placeholder.
        holder.bind(item, position, delegate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PagingStoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRowStoryBinding.inflate(layoutInflater, parent, false)
        return PagingStoryViewHolder(binding)
    }

}