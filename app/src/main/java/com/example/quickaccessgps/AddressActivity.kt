package com.example.quickaccessgps

import android.location.Geocoder
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class AddressActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var map: GoogleMap
    private lateinit var location: LatLng
    private lateinit var navigateButton: Button
    private lateinit var shareButton: Button
    private lateinit var address: Address

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        address = AddressSingleton.addresses[intent.getIntExtra("addressIndex", 0)]

        val addressNameText: TextView = findViewById(R.id.address_name)
        addressNameText.text = address.name

        val addressText: TextView = findViewById(R.id.address_address)
        addressText.text = address.address

        navigateButton = findViewById(R.id.navigate)
        shareButton = findViewById(R.id.share)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        val latLng = getLocationFromAddress(address.address)

        if (latLng != null) {
            location = latLng
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
}