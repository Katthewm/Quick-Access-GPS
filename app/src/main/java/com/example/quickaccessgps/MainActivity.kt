package com.example.quickaccessgps

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView: RecyclerView = findViewById(R.id.address_rv)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AddressRecyclerViewAdapter(arrayOf())

    }

    fun addAddress() {
        Toast.makeText(this, "Adding Address", Toast.LENGTH_LONG).show()
    }

    fun deleteAddress() {
        Toast.makeText(this, "Deleting Address", Toast.LENGTH_LONG).show()
    }

    fun shareAddress() {
        Toast.makeText(this, "Sharing Address", Toast.LENGTH_LONG).show()
    }
}