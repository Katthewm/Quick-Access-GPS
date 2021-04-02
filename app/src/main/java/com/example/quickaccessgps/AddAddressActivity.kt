package com.example.quickaccessgps

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity

class AddAddressActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_address)

        val cancelButton: ImageButton = findViewById(R.id.cancel_add_address)
        val editAddressName: EditText = findViewById(R.id.address_name)
        val editAddress: EditText = findViewById(R.id.address_address)
        val submitButton: Button = findViewById(R.id.address_submit)

        submitButton.setOnClickListener {
            AddressSingleton.addAddress(
                Address(
                    editAddressName.text.toString(),
                    editAddress.text.toString(),
                    false
                )
            )
            returnToMainActivity()
        }

        cancelButton.setOnClickListener { returnToMainActivity() }
    }

    private fun returnToMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}