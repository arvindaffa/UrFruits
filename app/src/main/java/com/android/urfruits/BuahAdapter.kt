package com.android.urfruits

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import coil.load
import com.android.response.History
import com.android.urfruits.databinding.ItemLayoutBuahBinding
import java.text.SimpleDateFormat
import java.util.Locale


class BuahAdapter : RecyclerView.Adapter<BuahAdapter.BuahViewHolder>() {

    private var buahList: List<History> = emptyList()
    var onItemClick: ((History) -> Unit)? = null

    class BuahViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val namaBuah: TextView = itemView.findViewById(R.id.tvBuah)
        val descBuah: TextView = itemView.findViewById(R.id.descBuah)
        val imageView: ImageView = itemView.findViewById(R.id.ivBuah)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BuahViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_layout_buah, parent, false)
        return BuahViewHolder(view)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: List<History>) {
        buahList = data
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: BuahViewHolder, position: Int) {
        val buah = buahList[position]

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        holder.namaBuah.text = buah.name?.capitalize(Locale.getDefault()) ?: buah.nama.orEmpty().capitalize(
            Locale.getDefault())

        val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val date = dateFormat.parse(buah.createdAt.orEmpty())
        val formattedDate = date?.let {
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(date)
        }
        holder.descBuah.text = "Di scan pada tanggal : ${formattedDate}"

        holder.imageView.load(buah.image) {
            crossfade(true)
            placeholder(circularProgressDrawable)
        }

        holder.itemView.setOnClickListener {
            onItemClick?.invoke(buah)
        }
    }

    override fun getItemCount(): Int = buahList.size
}