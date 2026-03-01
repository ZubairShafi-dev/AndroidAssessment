package com.example.androidassessment.presentation.randomusers

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidassessment.databinding.ItemRandomUserBinding
import com.example.androidassessment.domain.model.RandomUser

class RandomUserAdapter : ListAdapter<RandomUser, RandomUserAdapter.RandomUserViewHolder>(DiffCallback) {

    private object DiffCallback : DiffUtil.ItemCallback<RandomUser>() {
        override fun areItemsTheSame(old: RandomUser, new: RandomUser) = old.id == new.id
        override fun areContentsTheSame(old: RandomUser, new: RandomUser) = old == new
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RandomUserViewHolder =
        RandomUserViewHolder(
            ItemRandomUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )

    override fun onBindViewHolder(holder: RandomUserViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class RandomUserViewHolder(private val binding: ItemRandomUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(user: RandomUser) {
            binding.textViewRandomUserName.text  = user.fullName
            binding.textViewRandomUserEmail.text = user.email
            val location = listOfNotNull(
                user.city.takeIf { it.isNotEmpty() },
                user.state.takeIf { it.isNotEmpty() }
            ).joinToString(", ")
            binding.textViewRandomUserLocation.text = location.ifEmpty { "—" }
        }
    }
}
