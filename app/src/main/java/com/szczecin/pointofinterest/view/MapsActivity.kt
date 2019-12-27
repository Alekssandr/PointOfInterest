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
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.*
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.Polyline
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.maps.android.PolyUtil
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.adapter.ImageItemsAdapter
import com.szczecin.pointofinterest.adapter.StepItemsAdapter
import com.szczecin.pointofinterest.articles.model.GeoSearchPages
import com.szczecin.pointofinterest.common.ViewModelFactory
import com.szczecin.pointofinterest.common.extensions.lifecircle.observeLifecyclesIn
import com.szczecin.pointofinterest.databinding.ActivityMapsBinding
import com.szczecin.pointofinterest.common.extensions.viewModel
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

    @Inject
    lateinit var factory: ViewModelFactory<GeoSearchViewModel>
    @Inject
    lateinit var factoryMarkerDetailsViewModel: ViewModelFactory<MarkerDetailsViewModel>

    private val geoSearchViewModel: GeoSearchViewModel by viewModel { factory }
    private val markerDescriptionViewModel: MarkerDetailsViewModel by viewModel { factoryMarkerDetailsViewModel }

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private lateinit var sheetBehavior: BottomSheetBehavior<NestedScrollView>
    private val PERMISSION_ID = 42
    private lateinit var mRoutePolyline: Polyline
    lateinit var mFusedLocationClient: FusedLocationProviderClient

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)

        super.onCreate(savedInstanceState)

        setBinding()

        observeLifecyclesIn(listOf(geoSearchViewModel, markerDescriptionViewModel))

        geoSearchViewModel.setMapsPoint(this)

        setMapFragment()

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        addSheetBehavior()

        initRecycler()
    }

    private fun setMapFragment() {
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    private fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_maps)
        binding.viewModelGeoSearch = geoSearchViewModel
        binding.viewModelMarkerDetails = markerDescriptionViewModel
    }

    private fun addSheetBehavior() {
        sheetBehavior = BottomSheetBehavior.from<NestedScrollView>(bottom_sheet)
        sheetBehavior.isHideable = true
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
    }

    private fun expandCloseSheet() {
        when {
            sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN -> sheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
            sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED -> sheetBehavior.state =
                BottomSheetBehavior.STATE_COLLAPSED
            sheetBehavior.state == BottomSheetBehavior.STATE_COLLAPSED -> sheetBehavior.state =
                BottomSheetBehavior.STATE_EXPANDED
        }
    }

    override fun addMapsPoint(point: List<GeoSearchPages>) {
        point.forEach {
            val latLng = LatLng(it.lat, it.lon)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(it.title)
            )
            marker.tag = MarkerTag(it.pageId, latLng.latitude, latLng.longitude)
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.setOnMarkerClickListener(this)
        getLastLocation()
    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        binding.run {
            markerDescriptionViewModel.apply {
                p0?.tag.let {
                    val markerTag = it as MarkerTag
                    pageId.value = markerTag.id
                    markerLocation.value = "${markerTag.latitude},${markerTag.longitude}"
                    mMap.animateCamera(
                        MapsFactory.autoZoomCurrentLocation(
                            LatLng(
                                markerTag.latitude,
                                markerTag.longitude
                            )
                        )
                    )
                }
            }
            geoSearchViewModel.apply {
                routeSuggestionVisibility.value = false
            }
        }

        clearMarkersAndRoute()
        expandCloseSheet()
        return true
    }

    private fun initRecycler() {

        val recyclerImages = binding.bottomSheet.recyclerImages
        recyclerImages.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            this.adapter = ImageItemsAdapter()
        }

        val recyclerRoute = binding.bottomSheet.recyclerRouteSuggestion
        recyclerRoute.apply {
            this.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            this.adapter = StepItemsAdapter()
        }
    }

    private fun setUserPosition(location: Location) {
        binding.run {
            geoSearchViewModel.apply {
                locationCoordinates.value = "${location.latitude}|${location.longitude}"
            }
            lifecycleOwner = this@MapsActivity
        }
        mMap.animateCamera(
            MapsFactory.autoZoomCurrentLocation(LatLng(location.latitude, location.longitude))
        )
    }

    private fun setMarkersAndRoute(route: Route) {
        sheetBehavior.isHideable = false

        expandCloseSheet()

        val polylineOptions = MapsFactory.drawRoute(this)
        val pointsList = PolyUtil.decode(route.overviewPolyline)
        for (point in pointsList) {
            polylineOptions.add(point)
        }

        mRoutePolyline = mMap.addPolyline(polylineOptions)

        autoZoomLevel(route)
    }

    private fun autoZoomLevel(route: Route) {
        mMap.animateCamera(
            MapsFactory.autoZoomRoute(
                LatLng(route.startLat!!, route.startLng!!),
                LatLng(route.endLat!!, route.endLng!!)
            )
        )
    }

    override fun getRoute(route: Route) {
        setMarkersAndRoute(route)
    }

    private fun clearMarkersAndRoute() {
        if (::mRoutePolyline.isInitialized) {
            mRoutePolyline.remove()
        }
    }

    private val mLocationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult) {
            val mLastLocation: Location = locationResult.lastLocation
            setUserPosition(mLastLocation)
        }
    }

    @SuppressLint("MissingPermission")
    private fun getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mMap.isMyLocationEnabled = true
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    val location: Location? = task.result
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

    @SuppressLint("MissingPermission")
    private fun requestNewLocationData() {
        val mLocationRequest = LocationRequest()
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

    private fun isLocationEnabled(): Boolean {
        var locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
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
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_ID
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == PERMISSION_ID) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation()
            }
        }
    }
}