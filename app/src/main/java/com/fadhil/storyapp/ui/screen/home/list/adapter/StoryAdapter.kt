package com.fadhil.storyapp.ui.screen.home.list.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.fadhil.storyapp.databinding.ItemRowStoryBinding
import com.fadhil.storyapp.domain.model.Story

class StoryAdapter : RecyclerView.Adapter<StoryViewHolder>() {

    private val mList: MutableList<Story> = mutableListOf()
    var delegate: StoryDelegate? = null
    private lateinit var binding: ItemRowStoryBinding

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        binding = ItemRowStoryBinding.inflate(layoutInflater, parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bind(mList, position, delegate)
    }

    override fun getItemCount() = mList.size

    @SuppressLint("NotifyDataSetChanged")
    fun setData(newList: List<Story>) {
        mList.clear()
        mList.addAll(newList)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearData() {
        mList.clear()
        notifyDataSetChanged()
    }

}