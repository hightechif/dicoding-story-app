package com.fadhil.storyapp.ui.screen.home.list.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fadhil.storyapp.databinding.ItemRowStoryBinding
import com.fadhil.storyapp.domain.model.Story

class StoryAdapter : RecyclerView.Adapter<StoryViewHolder>() {

    var delegate: StoryDelegate? = null
    private lateinit var binding: ItemRowStoryBinding

    private val diffUtil = object : DiffUtil.ItemCallback<Story>() {
        override fun areItemsTheSame(oldItem: Story, newItem: Story):
                Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Story, newItem: Story):
                Boolean {
            return oldItem == newItem
        }
    }

    private val asyncListDiffer = AsyncListDiffer(this, diffUtil)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRowStoryBinding.inflate(layoutInflater, parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(asyncListDiffer.currentList, position, delegate)
    }

    override fun getItemCount() = asyncListDiffer.currentList.size

    fun setData(newList: List<Story>) {
        asyncListDiffer.submitList(newList)
    }

    fun clearData() {
        asyncListDiffer.submitList(emptyList())
    }

}