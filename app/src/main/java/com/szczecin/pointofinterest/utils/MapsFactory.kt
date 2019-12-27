package com.szczecin.pointofinterest.utils

import android.content.Context
import android.os.Build
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.PolylineOptions
import com.szczecin.pointofinterest.R

object MapsFactory {

    fun autoZoomCurrentLocation(currentLocation: LatLng): CameraUpdate {
        return CameraUpdateFactory.newLatLngZoom(currentLocation, 14f)
    }

    fun autoZoomRoute(start: LatLng, finish: LatLng): CameraUpdate {
        val builder = LatLngBounds.Builder()

        builder.include(start)
        builder.include(finish)

        val bounds = builder.build()

        val padding = 200

        return CameraUpdateFactory.newLatLngBounds(bounds, padding)
    }

    fun drawRoute(context: Context): PolylineOptions {
        val polylineOptions = PolylineOptions()
        polylineOptions.width(DisplayUtility.px2dip(context, 72.toFloat()).toFloat())
        polylineOptions.geodesic(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            polylineOptions.color(context.resources.getColor(R.color.colorAccent, context.theme))
        } else {
            polylineOptions.color(context.resources.getColor(R.color.colorPrimaryDark))
        }
        return polylineOptions
    }
}