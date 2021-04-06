package com.example.quickaccessgps.share

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.quickaccessgps.DataSingleton
import com.example.quickaccessgps.R

class ShareRecyclerViewAdapter(private val onItemClick: (share: Share) -> Unit) :
    RecyclerView.Adapter<ShareRecyclerViewAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val addressTextView: TextView = itemView.findViewById(R.id.address_text)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.share_list_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val shares = DataSingleton.shares
        holder.addressTextView.text =
            "${shares[position].from} shared ${shares[position].name} with you."

        holder.itemView.setOnClickListener { onItemClick(shares[position]) }
    }

    override fun getItemCount(): Int {
        return DataSingleton.shares.size
    }
}
