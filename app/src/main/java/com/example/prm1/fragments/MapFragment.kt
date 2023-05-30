package com.example.prm1.fragments

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Geocoder
import android.location.LocationManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import com.example.prm1.databinding.FragmentMapBinding
import com.example.prm1.model.SettingsNote
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.util.*

class MapFragment : Fragment() {
    private lateinit var binding: FragmentMapBinding
    private lateinit var geoPoint: GeoPoint

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentMapBinding.inflate(inflater,  container, false).also {
            binding = it
        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Configuration.getInstance().userAgentValue = requireContext().packageName

        if(
            requireContext().checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
            && requireContext().checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
        ) {
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions()
            ){
                    if (it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true){
                        locationOn()
                    }
            }.launch(
                arrayOf(android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION)
            )
        } else {
            locationOn()
        }

        binding.map.apply {
            setTileSource(TileSourceFactory.MAPNIK)
        }


    }

    @SuppressLint("MissingPermission")
    private fun locationOn() {
        binding.map.apply {
            overlays.add(MyLocationNewOverlay(this).apply { enableMyLocation() })
            requireContext().getSystemService(LocationManager::class.java).getLastKnownLocation(LocationManager.GPS_PROVIDER)
                ?.let {
                    geoPoint = GeoPoint(it.latitude, it.longitude)
                    controller.animateTo(geoPoint, 21.0, 3000)
                    val geocoder = Geocoder(context)
                    val addresses = geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)
                    binding.textView.text =
                        ("ul. " + addresses?.get(0)?.thoroughfare + ", " + addresses?.get(0)?.locality + ", " + addresses?.get(0)?.countryName) ?: ""
                    changeLocation()
                }
        }
    }

    private fun changeLocation(){
        (parentFragmentManager.findFragmentByTag(EditFragment::class.java.name) as? EditFragment)?.let {
            it.setLocation(binding.textView.text.toString())
        }
    }
}
