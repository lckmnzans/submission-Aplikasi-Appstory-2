package com.submission.appstory.paging.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.submission.appstory.databinding.ItemStoriesBinding
import com.submission.appstory.response.StoryItem
import com.submission.appstory.Story

class StoryListAdapter: PagingDataAdapter<StoryItem, StoryListAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: Story)
        fun onItemMapsClicked(data: Story)
    }

    class MyViewHolder(private val binding: ItemStoriesBinding): RecyclerView.ViewHolder(binding.root) {
        val ivPhoto = binding.ivItemPhoto
        val tvStory = binding.tvItemName
        val ivLoc = binding.ivMapsAvailability
        fun bind(data: StoryItem) {
            this.tvStory.text = data.name
            Glide.with(this.itemView.context).load(data.photoUrl).into(this.ivPhoto)
            if (!data.lon.isNullOrEmpty() && !data.lat.isNullOrEmpty()) {
                ivLoc.visibility = View.VISIBLE
            } else {
                ivLoc.visibility = View.GONE
            }
        }
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
            val dataItem = Story(data.id, data.name, data.photoUrl, data.description, data.lat, data.lon)
            holder.itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(dataItem)
            }
            holder.ivLoc.setOnClickListener {
                onItemClickCallback.onItemMapsClicked(dataItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
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