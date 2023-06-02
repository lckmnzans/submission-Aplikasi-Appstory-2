package com.submission.appstory.paging.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.appstory.databinding.ItemStoriesBinding
import com.submission.appstory.response.StoryItem

class StoryListAdapter: PagingDataAdapter<StoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    class MyViewHolder(private val binding: ItemStoriesBinding): RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivItemPhoto
        val tvStory = binding.tvItemName
        fun bind(data: StoryItem) {
            this.tvStory.text = data.name
            Glide.with(this.itemView.context).load(data.photoUrl).into(this.ivPhoto)
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryItem>() {
            override fun areItemsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryItem, newItem: StoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}