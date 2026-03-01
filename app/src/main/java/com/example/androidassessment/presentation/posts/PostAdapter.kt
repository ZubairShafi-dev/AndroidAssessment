package com.example.androidassessment.presentation.posts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidassessment.databinding.ItemPostBinding
import com.example.androidassessment.domain.model.Post

class PostAdapter(
    private val onPostClick: (Post) -> Unit
) : ListAdapter<Post, PostAdapter.PostViewHolder>(PostDiffCallback) {

    private object PostDiffCallback : DiffUtil.ItemCallback<Post>() {
        override fun areItemsTheSame(old: Post, new: Post) = old.id == new.id
        override fun areContentsTheSame(old: Post, new: Post) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder =
        PostViewHolder(
            ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bind(getItem(position), onPostClick)
    }

    class PostViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(post: Post, onClick: (Post) -> Unit) {
            binding.textViewPostTitle.text = post.title
                .replaceFirstChar { it.uppercaseChar() }
            binding.textViewPostBody.text = post.body
            itemView.setOnClickListener { onClick(post) }
        }
    }
}
