package com.example.quickaccessgps

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val addAddressButton: ImageButton = findViewById(R.id.add_address)
        addAddressButton.setOnClickListener { addAddress() }

        recyclerView = findViewById(R.id.address_rv)
        recyclerView.adapter = AddressRecyclerViewAdapter(AddressSingleton.addresses)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
    }

    private fun addAddress() {
        val intent = Intent(this, AddAddressActivity::class.java)
        startActivity(intent)
    }

    fun deleteAddress() {
        Toast.makeText(this, "Deleting Address", Toast.LENGTH_LONG).show()
    }

    fun shareAddress() {
        Toast.makeText(this, "Sharing Address", Toast.LENGTH_LONG).show()
    }
}
