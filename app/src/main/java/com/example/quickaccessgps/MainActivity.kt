package com.example.quickaccessgps

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity(), DeleteAddressDialogFragment.DeleteAddressDialogListener {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddressRecyclerViewAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val addAddressButton: ImageButton = findViewById(R.id.add_address)
        addAddressButton.setOnClickListener { addAddress() }

        recyclerView = findViewById(R.id.address_rv)
        adapter = AddressRecyclerViewAdapter(
            AddressSingleton.addresses,
            { },
            { addressIndex -> showDeleteDialog(addressIndex) }
        )
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun showDeleteDialog(addressIndex: Int) {
        val dialog = DeleteAddressDialogFragment().apply {
            arguments = Bundle().apply { putInt("addressIndex", addressIndex) }
        }

        dialog.show(supportFragmentManager, "DeleteAddressDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, addressIndex: Int) {
        AddressSingleton.removeAddress(addressIndex)
        adapter.notifyDataSetChanged()
    }

    private fun addAddress() {
        val intent = Intent(this, AddAddressActivity::class.java)
        startActivity(intent)
    }
}
