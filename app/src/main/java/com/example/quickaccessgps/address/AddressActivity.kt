package com.example.quickaccessgps.address

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import com.example.quickaccessgps.DataSingleton
import com.example.quickaccessgps.R
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddressActivity : AppCompatActivity(), OnMapReadyCallback,
    ShareAddressDialogFragment.ShareAddressDialogListener {

    private lateinit var map: GoogleMap
    private lateinit var navigateButton: Button
    private lateinit var shareButton: Button
    private lateinit var address: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        address = DataSingleton.addresses?.get(intent.getIntExtra("addressIndex", 0)) ?: Address(
            "",
            "",
            false
        )

        val addressNameText: TextView = findViewById(R.id.address_name)
        addressNameText.text = address.name

        val addressText: TextView = findViewById(R.id.address_address)
        addressText.text = address.address

        navigateButton = findViewById(R.id.navigate)
        navigateButton.setOnClickListener { navigate() }

        shareButton = findViewById(R.id.share)
        shareButton.setOnClickListener { share() }
    }

    private fun share() {
        val dialog = ShareAddressDialogFragment().apply {
            arguments = Bundle().apply { putString("addressId", address.id) }
        }

        dialog.show(supportFragmentManager, "ShareAddressDialogFragment")
    }

    private fun navigate() {
        val intentUri = Uri.parse("google.navigation:q=${address.address.replace(" ", "+")}")
        val intent = Intent(Intent.ACTION_VIEW, intentUri)
        intent.setPackage("com.google.android.apps.maps")
        startActivity(intent)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val location = getLocationFromAddress(address.address)

        if (location != null) {
            map.addMarker(MarkerOptions().position(location).title(address.name))
            map.moveCamera(CameraUpdateFactory.newLatLng(location))
            map.animateCamera(CameraUpdateFactory.zoomTo(15f))
        } else {
            navigateButton.isEnabled = false
            shareButton.isEnabled = false
            // TODO: Should show some error
        }
    }

    private fun getLocationFromAddress(address: String): LatLng? {
        val geocoder = Geocoder(this)
        val location = geocoder.getFromLocationName(address, 5)

        return if (location.size > 0) LatLng(location[0].latitude, location[0].longitude) else null
    }

    override fun onDialogPositiveClick(
        dialog: DialogFragment,
        targetEmail: String,
        address: Address
    ) {
        DataSingleton.sendShare(targetEmail, address)?.addOnSuccessListener {
            Toast.makeText(this@AddressActivity, "Shared successfully!", Toast.LENGTH_SHORT).show()
        }
    }
}