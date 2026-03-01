package com.example.androidassessment.presentation.breeds

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.androidassessment.R
import com.example.androidassessment.domain.model.Breed

/**
 * Expandable [ListAdapter] for the breeds screen.
 *
 * Each item shows the breed name + sub-breed count in a header row.
 * Tapping the header toggles a dynamically inflated list of sub-breed
 * rows inside [layoutSubBreeds] — no nested RecyclerView needed.
 */
class BreedAdapter : ListAdapter<BreedAdapter.BreedUiItem, BreedAdapter.BreedViewHolder>(BreedDiffCallback) {

    // ── Model ──────────────────────────────────────────────────────────────────

    /**
     * Wraps [Breed] with local expand state so toggling one item doesn't
     * invalidate the entire list.
     */
    data class BreedUiItem(
        val breed: Breed,
        var isExpanded: Boolean = false
    )

    // ── DiffUtil ───────────────────────────────────────────────────────────────

    private object BreedDiffCallback : DiffUtil.ItemCallback<BreedUiItem>() {
        override fun areItemsTheSame(old: BreedUiItem, new: BreedUiItem) =
            old.breed.id == new.breed.id

        override fun areContentsTheSame(old: BreedUiItem, new: BreedUiItem) =
            old == new
    }

    // ── Public helpers ────────────────────────────────────────────────────────

    /** Replace the full list, preserving existing expand states by breed id. */
    fun submitBreeds(breeds: List<Breed>) {
        val expandedIds = currentList.filter { it.isExpanded }.map { it.breed.id }.toSet()
        submitList(breeds.map { breed ->
            BreedUiItem(breed, isExpanded = breed.id in expandedIds)
        })
    }

    /** Filter visible items by a search query (case-insensitive, start-of-word). */
    fun filter(query: String, fullList: List<Breed>) {
        val filtered = if (query.isBlank()) fullList
        else fullList.filter { it.name.contains(query, ignoreCase = true) }
        submitBreeds(filtered)
    }

    // ── ViewHolder ─────────────────────────────────────────────────────────────

    inner class BreedViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvBreedName: TextView       = view.findViewById(R.id.textViewBreedName)
        private val tvSubCount: TextView        = view.findViewById(R.id.textViewSubBreedCount)
        private val ivExpand: ImageView         = view.findViewById(R.id.imageViewExpand)
        private val layoutHeader: View          = view.findViewById(R.id.layoutBreedHeader)
        private val layoutSubBreeds: LinearLayout = view.findViewById(R.id.layoutSubBreeds)
        private val divider: View               = view.findViewById(R.id.divider)

        fun bind(item: BreedUiItem) {
            val breed = item.breed

            // ── Header ─────────────────────────────────────────────────────────
            tvBreedName.text = breed.name.replaceFirstChar { it.uppercaseChar() }

            tvSubCount.text = when {
                breed.subBreeds.isEmpty() ->
                    itemView.context.getString(R.string.label_no_sub_breeds)
                else ->
                    itemView.context.getString(R.string.label_sub_breeds, breed.subBreeds.size)
            }

            // ── Expand state ───────────────────────────────────────────────────
            applyExpandState(item, animated = false)

            layoutHeader.setOnClickListener {
                item.isExpanded = !item.isExpanded
                applyExpandState(item, animated = true)
            }
        }

        private fun applyExpandState(item: BreedUiItem, animated: Boolean) {
            val expanded = item.isExpanded
            val breed    = item.breed

            // Rotate expand arrow
            ivExpand.animate()
                .rotation(if (expanded) 180f else 0f)
                .setDuration(if (animated) 200L else 0L)
                .start()

            // Show/hide sub-breed list and divider
            if (expanded) {
                layoutSubBreeds.visibility = View.VISIBLE
                divider.visibility         = View.VISIBLE
                populateSubBreeds(breed)
            } else {
                layoutSubBreeds.visibility = View.GONE
                divider.visibility         = View.GONE
                layoutSubBreeds.removeAllViews()
            }
        }

        private fun populateSubBreeds(breed: Breed) {
            layoutSubBreeds.removeAllViews()
            val inflater = LayoutInflater.from(itemView.context)
            breed.subBreeds.forEach { subBreed ->
                val row = inflater.inflate(
                    R.layout.item_breed_child,
                    layoutSubBreeds,
                    false
                ) as TextView
                row.text = "• ${subBreed.replaceFirstChar { it.uppercaseChar() }}"
                layoutSubBreeds.addView(row)
            }
        }
    }

    // ── Adapter overrides ─────────────────────────────────────────────────────

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreedViewHolder =
        BreedViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_breed_parent, parent, false)
        )

    override fun onBindViewHolder(holder: BreedViewHolder, position: Int) =
        holder.bind(getItem(position))
}
