package com.mobdeve.s12.group8.glimpse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mobdeve.s12.group8.glimpse.databinding.ActivityHomeBinding
import com.mobdeve.s12.group8.glimpse.databinding.ActivityLocationBinding

class LocationActivity: AppCompatActivity() {
    private lateinit var binding: ActivityLocationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val latitude = intent.getDoubleExtra("LATITUDE", 0.0)
        val longitude = intent.getDoubleExtra("LONGITUDE", 0.0)
        val latLng = LatLng(latitude, longitude)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.mapFragment) as SupportMapFragment
        mapFragment.getMapAsync { googleMap ->
            onMapReady(googleMap, latLng)
        }

        binding.locationExitBtn.setOnClickListener {
            finish()
        }
    }

    private fun onMapReady(googleMap: GoogleMap, latLng: LatLng) {
        // Add a marker and move the camera to the provided LatLng
        googleMap.addMarker(MarkerOptions().position(latLng).title("Selected Location"))
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15.5f))
    }

}