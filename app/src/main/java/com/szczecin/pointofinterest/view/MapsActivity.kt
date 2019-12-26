package com.szczecin.pointofinterest.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.adapter.ImageItemsAdapter
import com.szczecin.pointofinterest.common.ViewModelFactory
import com.szczecin.pointofinterest.common.lifecircle.extensions.observeLifecyclesIn
import com.szczecin.pointofinterest.databinding.ActivityMapsBinding
import com.szczecin.pointofinterest.entities.marker.Article
import com.szczecin.pointofinterest.route.model.Route
import com.szczecin.pointofinterest.utils.MapsFactory
import com.szczecin.pointofinterest.utils.MarkerTag
import com.szczecin.pointofinterest.viewmodel.GeoSearchViewModel
import com.szczecin.pointofinterest.viewmodel.MapsPoint
import com.szczecin.pointofinterest.viewmodel.MarkerDetailsViewModel
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.bottom_sheet.*
import javax.inject.Inject


class MapsActivity : AppCompatActivity(), OnMapReadyCallback, MapsPoint,
    GoogleMap.OnMarkerClickListener {

    private lateinit var mMap: GoogleMap

    private lateinit var binding: ActivityMapsBinding

    private lateinit var sheetBehavior: BottomSheetBehavior<NestedScrollView>

    val PERMISSION_ID = 42
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    @Inject
    lateinit var factory: ViewModelFactory<GeoSearchViewModel>

    @Inject
    lateinit var factoryMarkerDetailsViewModel: ViewModelFactory<MarkerDetailsViewModel>

    private val mainViewModel: GeoSearchViewModel by viewModel { factory }
    private val markerDescriptionViewModel: MarkerDetailsViewModel by viewModel { factoryMarkerDetailsViewModel }

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        binding.viewModelGeoSearch = mainViewModel
        binding.viewModelMarkerDetails = markerDescriptionViewModel
        observeLifecyclesIn(listOf(mainViewModel, markerDescriptionViewModel))
        mainViewModel.setMapsPoint(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        sheetBehavior = BottomSheetBehavior.from<NestedScrollView>(bottom_sheet)
        sheetBehavior.isHideable = true
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        initRecycler()
    }


    private fun expandCloseSheet() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }  else if (sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun addMapsPoint(point: List<Article>) {
        point.forEach {
            val latLng = LatLng(it.lat, it.lon)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(it.title)
            )
            marker.tag = MarkerTag(it.pageId,"${latLng.latitude},${latLng.longitude}")
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        getLastLocation()
        mMap.setOnMarkerClickListener(this)
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        binding.run {
            markerDescriptionViewModel.apply {
                val markerTag = p0?.tag as MarkerTag
                pageId.value = markerTag.id
                markerLocation.value = markerTag.location
            }
            lifecycleOwner = this@MapsActivity
        }

        expandCloseSheet()
        return true
    }

    private fun initRecycler() {

        val recyclerImages = binding.bottomSheet.recyclerImages

        recyclerImages.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = ImageItemsAdapter()
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mMap.isMyLocationEnabled = true
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    var location: Location? = task.result
                    if (location == null) {
                        requestNewLocationData()
                    } else {
                        setUserPosition(location)
                    }
                }
            } else {
                Toast.makeText(this, "Turn on location", Toast.LENGTH_LONG).show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun setUserPosition(location: Location) {
        binding.run {
            mainViewModel.apply {
                locationCoordinates.value = "${location.latitude}|${location.longitude}"

            }
            lifecycleOwner = this@MapsActivity
        }
        val userPosition = LatLng(location.latitude, location.longitude)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(userPosition))
        mMap.setMinZoomPreference(14.0f)
        mMap.setMaxZoomPreference(16.0f)
    }

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        var mLocationRequest = LocationRequest()
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        mLocationRequest.interval = 0
        mLocationRequest.fastestInterval = 0
        mLocationRequest.numUpdates = 1

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        mFusedLocationClient.requestLocationUpdates(
            mLocationRequest, mLocationCallback,
            Looper.myLooper()
        )
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            var mLastLocation: Location = locationResult.lastLocation
            setUserPosition(mLastLocation)
        }
    }

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION),
            PERMISSION_ID
        )
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }

    private var mRouteMarkerList = ArrayList<Marker>()
    private lateinit var mRoutePolyline: Polyline

    private fun setMarkersAndRoute(route: Route) {
        clearMarkersAndRoute()
        expandCloseSheet()
        val startLatLng = LatLng(route.startLat!!, route.startLng!!)
        val startMarkerOptions: MarkerOptions = MarkerOptions().position(startLatLng).title(route.startName).icon(
            BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(this, "S")))
        val endLatLng = LatLng(route.endLat!!, route.endLng!!)
        val endMarkerOptions: MarkerOptions = MarkerOptions().position(endLatLng).title(route.endName).icon(
            BitmapDescriptorFactory.fromBitmap(MapsFactory.drawMarker(this, "E")))
        val startMarker = mMap.addMarker(startMarkerOptions)
        val endMarker = mMap.addMarker(endMarkerOptions)
        mRouteMarkerList.add(startMarker)
        mRouteMarkerList.add(endMarker)

        val polylineOptions = MapsFactory.drawRoute(this)
        val pointsList = PolyUtil.decode(route.overviewPolyline)
        for (point in pointsList) {
            polylineOptions.add(point)
        }

        mRoutePolyline = mMap.addPolyline(polylineOptions)

        mMap.animateCamera(MapsFactory.autoZoomLevel(mRouteMarkerList))
    }

    override fun getRoute(route: Route) {
        setMarkersAndRoute(route)
    }

    private fun clearMarkersAndRoute() {
        for (marker in mRouteMarkerList) {
            marker.remove()
        }
        mRouteMarkerList.clear()

        if (::mRoutePolyline.isInitialized) {
            mRoutePolyline.remove()
        }
    }

    private inline fun <reified VM : ViewModel, F : ViewModelProvider.Factory> AppCompatActivity.viewModel(
        crossinline f: () -> F
    ): Lazy<VM> =
        lazy { ViewModelProviders.of(this, f.invoke()).get(VM::class.java) }
}