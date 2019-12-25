package com.szczecin.pointofinterest.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.szczecin.pointofinterest.R
import com.szczecin.pointofinterest.common.ViewModelFactory
import com.szczecin.pointofinterest.common.lifecircle.extensions.observeLifecyclesIn
import com.szczecin.pointofinterest.databinding.ActivityMapsBinding
import com.szczecin.pointofinterest.entities.marker.Article
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
        binding.viewModel = mainViewModel
        binding.viewModelMarkerDetails = markerDescriptionViewModel
        observeLifecyclesIn(listOf(mainViewModel, markerDescriptionViewModel))
        mainViewModel.setMapsPoint(this)

        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        sheetBehavior = BottomSheetBehavior.from<NestedScrollView>(bottom_sheet)
        sheetBehavior.isHideable = true
        sheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
//        /**
//         * bottom sheet state change listener
//         * we are changing button text when sheet changed state
//         * */
//        sheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                    }
//                    BottomSheetBehavior.STATE_EXPANDED ->
//                        btBottomSheet.text = "Close Bottom Sheet"
//                    BottomSheetBehavior.STATE_COLLAPSED ->
//                        btBottomSheet.text = "Expand Bottom Sheet"
//                    BottomSheetBehavior.STATE_DRAGGING -> {
//                    }
//                    BottomSheetBehavior.STATE_SETTLING -> {
//                    }
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//            }
//        })
//
//        btBottomSheet.setOnClickListener(View.OnClickListener {
//            expandCloseSheet()
//        })

    }


    private fun expandCloseSheet() {
        if (sheetBehavior.state == BottomSheetBehavior.STATE_HIDDEN) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        } else if (sheetBehavior.state == BottomSheetBehavior.STATE_EXPANDED) {
            sheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
        }
//        } else {
//            sheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
//        }
    }

    override fun addMapsPoint(point: List<Article>) {
        point.forEach {
            val latLng = LatLng(it.lat, it.lon)
            val marker = mMap.addMarker(
                MarkerOptions()
                    .position(latLng)
                    .title(it.title)
            )
            marker.tag = it.pageId
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val sydney = LatLng(60.181666666666665, 24.929722222222225)
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.setMinZoomPreference(14.0f)
        mMap.setMaxZoomPreference(16.0f)
        mMap.setOnMarkerClickListener(this)

    }

    override fun onMarkerClick(p0: Marker?): Boolean {
        binding.run {
            markerDescriptionViewModel.apply {
                pageId.value = p0?.tag as String
            }
            lifecycleOwner = this@MapsActivity
        }

        expandCloseSheet()
        return true
    }

    private inline fun <reified VM : ViewModel, F : ViewModelProvider.Factory> AppCompatActivity.viewModel(
        crossinline f: () -> F
    ): Lazy<VM> =
        lazy { ViewModelProviders.of(this, f.invoke()).get(VM::class.java) }
}