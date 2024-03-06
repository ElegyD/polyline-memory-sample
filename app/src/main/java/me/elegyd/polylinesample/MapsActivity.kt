package me.elegyd.polylinesample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.Dash
import com.google.android.gms.maps.model.Dot
import com.google.android.gms.maps.model.Gap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Polyline
import com.google.android.gms.maps.model.PolylineOptions
import me.elegyd.polylinesample.databinding.ActivityMapsBinding

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // FIXME: Switch renderer to LEGACY for https://issuetracker.google.com/issues/318773921
        MapsInitializer.initialize(applicationContext, MapsInitializer.Renderer.LATEST) {
            when (it) {
                MapsInitializer.Renderer.LATEST -> Log.d("MapsActivity", "The latest version of the renderer is used.")
                MapsInitializer.Renderer.LEGACY -> Log.d("MapsActivity", "The legacy version of the renderer is used.")
            }
        }

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(37.4, -122.1), 11F))

        val runtime = Runtime.getRuntime()
        var polyline: Polyline? = null

        val handler = Handler(Looper.myLooper()!!)
        handler.postDelayed(object : Runnable {
            override fun run() {
                polyline?.remove()

                val before = runtime.totalMemory() - runtime.freeMemory()
                Log.d("MapsActivity", "$before bytes allocated")

                val pattern = listOf(
                    Dot(), Gap(20F), Dash(30F), Gap(20F)
                )

                // Instantiates a new Polyline object and adds points to define a rectangle
                val polylineOptions = PolylineOptions()
                    .add(LatLng(37.35, -122.0))
                    .add(LatLng(37.45, -122.0)) // North of the previous point, but at the same longitude
                    .add(LatLng(37.45, -122.2)) // Same latitude, and 30km to the west
                    .add(LatLng(37.35, -122.2)) // Same longitude, and 16km to the south
                    .add(LatLng(37.35, -122.0)) // Closes the polyline.
                    .pattern(pattern)

                // Get back the mutable Polyline
                polyline = mMap.addPolyline(polylineOptions)

                val after = runtime.totalMemory() - runtime.freeMemory()
                Log.d("MapsActivity", "$after bytes allocated, increased by ${after - before} bytes")

                handler.postDelayed(this, 5000)
            }
        }, 5000)
    }
}