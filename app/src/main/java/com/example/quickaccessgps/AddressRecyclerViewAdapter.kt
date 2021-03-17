package com.example.quickaccessgps

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AddressRecyclerViewAdapter(private val addresses: Array<String>) : RecyclerView.Adapter<AddressRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTextView: TextView = itemView.findViewById(R.id.address_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.address_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return addresses.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.addressTextView.text = addresses[position]
    }

}
