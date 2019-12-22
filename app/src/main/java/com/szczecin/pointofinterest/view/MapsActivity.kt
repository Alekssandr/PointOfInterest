package com.szczecin.pointofinterest.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.szczecin.pointofinterest.BuildConfig
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.common.ViewModelFactory
import com.szczecin.pointofinterest.common.lifecircle.extensions.observeLifecycleIn
import com.szczecin.pointofinterest.databinding.ActivityMapsBinding
import com.szczecin.pointofinterest.entities.Article
import com.szczecin.pointofinterest.viewmodel.GeoSearchViewModel
import com.szczecin.pointofinterest.viewmodel.MapsPoint
import dagger.android.AndroidInjection
import javax.inject.Inject

class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsPoint {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding

//    private val mainViewModel: GeoSearchViewModel by viewModel { factory }

    @Inject
    lateinit var factory: ViewModelFactory<GeoSearchViewModel>

    private val mainViewModel: GeoSearchViewModel by viewModel { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_maps)
        observeLifecycleIn(mainViewModel)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
//        val viewModel = ViewModelProviders.of(this).get(GeoSearchViewModel::class.java)
        binding.viewModel = mainViewModel
        mainViewModel.setMapsPoint(this)
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    override fun addMapsPoint(point: List<Article>) {
        point.forEach {
            val marker = LatLng(it.lat, it.lon)
            mMap.addMarker(MarkerOptions().position(marker).title(it.title))
        }
    }

    private inline fun <reified VM : ViewModel, F : ViewModelProvider.Factory> AppCompatActivity.viewModel(
        crossinline f: () -> F
    ): Lazy<VM> =
        lazy { ViewModelProviders.of(this, f.invoke()).get(VM::class.java) }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Sydney and move the camera
        val sydney = LatLng(60.181666666666665, 24.929722222222225)
//        mMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setMinZoomPreference(14.0f)
        mMap.setMaxZoomPreference(16.0f)
    }
}
