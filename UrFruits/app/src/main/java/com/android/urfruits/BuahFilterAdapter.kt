package com.android.urfruits

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.android.urfruits.databinding.ItemLayoutFilterBinding

data class BuahFilterData(
    val id: String,
    val name: String,
    var selected: Boolean? = false
)

class BuahFilterAdapter : ListAdapter<BuahFilterData, BuahFilterAdapter.ViewHolder>(BuahFilterDiffCallback()) {

    var onItemClick: ((BuahFilterData) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemLayoutFilterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemLayoutFilterBinding) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.btnFilter.setOnClickListener {
                onItemClick?.invoke(getItem(adapterPosition))
            }
        }

        fun bind(buahFilterData: BuahFilterData) {
            binding.apply {
                btnFilter.text = buahFilterData.name
                btnFilter.setBackgroundColor(binding.root.resources.getColor(
                    if (buahFilterData.selected == true) {
                        R.color.black
                    } else {
                        R.color.gray
                    }
                ))
            }
        }
    }

    class  BuahFilterDiffCallback : DiffUtil.ItemCallback<BuahFilterData>() {
        override fun areItemsTheSame(oldItem: BuahFilterData, newItem: BuahFilterData): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BuahFilterData, newItem: BuahFilterData): Boolean {
            return oldItem == newItem
        }
    }
}