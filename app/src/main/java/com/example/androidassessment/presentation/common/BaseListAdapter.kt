package com.example.androidassessment.presentation.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

/**
 * Generic [ListAdapter] base class to reduce boilerplate across feature adapters.
 *
 * @param T   The data type held by each item.
 * @param VH  The [RecyclerView.ViewHolder] type.
 */
abstract class BaseListAdapter<T : Any, VH : RecyclerView.ViewHolder>(
    diffCallback: DiffUtil.ItemCallback<T>
) : ListAdapter<T, VH>(diffCallback) {

    protected val inflater: (ViewGroup) -> LayoutInflater =
        { parent -> LayoutInflater.from(parent.context) }
}
