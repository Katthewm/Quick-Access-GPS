package com.example.quickaccessgps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.util.*

class AddressRecyclerViewAdapter(private val addresses: ArrayList<Address>) :
    RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTextView: TextView = itemView.findViewById(R.id.address_text)
        val favoriteAddressButton: ImageButton = itemView.findViewById(R.id.favorite_address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_list_item, parent, false)

        sortAddresses()

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.addressTextView.text = addresses[position].name
        setFavoriteButton(holder.favoriteAddressButton, addresses[position].isFavorite)

        holder.favoriteAddressButton.setOnClickListener {
            addresses[position].isFavorite = !addresses[position].isFavorite
            setFavoriteButton(holder.favoriteAddressButton, addresses[position].isFavorite)
            sortAddresses()
            this.notifyDataSetChanged()
        }
    }

    private fun sortAddresses() {
        addresses.sortWith(Comparator { first, second ->
            if (first.isFavorite && !second.isFavorite) {
                return@Comparator -1
            } else if (!first.isFavorite && second.isFavorite) {
                return@Comparator 1
            } else {
                return@Comparator first.name.compareTo(second.name)
            }
        })
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    private fun setFavoriteButton(button: ImageButton, isFavorite: Boolean) {
        if (isFavorite) {
            button.setImageResource(R.drawable.baseline_star_24)
        } else {
            button.setImageResource(R.drawable.baseline_star_border_black_24)
        }
    }
}
