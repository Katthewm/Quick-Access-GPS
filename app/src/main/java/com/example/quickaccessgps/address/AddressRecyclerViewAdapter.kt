package com.example.quickaccessgps.address

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickaccessgps.DataSingleton
import com.example.quickaccessgps.R

class AddressRecyclerViewAdapter(
    private val onItemClick: (position: Int) -> Unit,
    private val onItemLongClick: (position: Int) -> Unit
) : RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTextView: TextView = itemView.findViewById(R.id.address_text)
        val favoriteAddressButton: ImageButton = itemView.findViewById(R.id.favorite_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val addresses = DataSingleton.addresses ?: return
        holder.addressTextView.text = addresses[position].name
        setFavoriteButton(holder.favoriteAddressButton, addresses[position].isFavorite)

        holder.favoriteAddressButton.setOnClickListener {
            DataSingleton.setIsFavorite(position, !addresses[position].isFavorite)
            setFavoriteButton(holder.favoriteAddressButton, addresses[position].isFavorite)
            this.notifyDataSetChanged()
        }

        holder.itemView.setOnClickListener { onItemClick(position) }
        holder.itemView.setOnLongClickListener {
            onItemLongClick(position)
            true
        }
    }

    override fun getItemCount(): Int {
        return DataSingleton.addresses?.size ?: 0
    }

    private fun setFavoriteButton(button: ImageButton, isFavorite: Boolean) {
        if (isFavorite) {
            button.setImageResource(R.drawable.baseline_star_24)
        } else {
            button.setImageResource(R.drawable.baseline_star_border_black_24)
        }
    }
}
