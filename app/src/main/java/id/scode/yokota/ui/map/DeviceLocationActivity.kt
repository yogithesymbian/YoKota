/*
 * Copyright (c) 2019. Scodeid | yogithesymbian | Yogi Arif Widodo.
 * Android Studio 3.5.1
 * Build #AI-191.8026.42.35.5900203, built on September 26, 2019
 * JRE: 1.8.0_202-release-1483-b49-5587405 amd64
 * JVM: OpenJDK 64-Bit Server VM by JetBrains s.r.o
 * Linux 5.2.0-kali3-amd64
 */

package id.scode.yokota.ui.map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import id.scode.yokota.R

class DeviceLocationActivity : AppCompatActivity() {

    lateinit var supportMapFragment: SupportMapFragment
    lateinit var googleMap: GoogleMap

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_device_location)

        supportMapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        supportMapFragment.getMapAsync {

            googleMap = it

            val location = LatLng(-1.295124, 116.748026)
            googleMap.addMarker(MarkerOptions().position(location).title("HSR-004 Forestdroid 1"))
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))

            val location2 = LatLng(-1.398616, 116.654236)
            googleMap.addMarker(MarkerOptions().position(location2).title("HSR-004 Foresdroid 2"))
    //            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 10f))
        }
    }
}
