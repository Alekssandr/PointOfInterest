package com.szczecin.pointofinterest.entities.route

import com.google.gson.annotations.SerializedName
import com.szczecin.pointofinterest.entities.route.Bounds
import com.szczecin.pointofinterest.entities.route.LegsItem
import com.szczecin.pointofinterest.entities.route.OverviewPolyline

data class RoutesItem(@SerializedName("summary")
                      val summary: String? = "",
                      @SerializedName("copyrights")
                      val copyrights: String? = "",
                      @SerializedName("legs")
                      val legs: List<LegsItem>,
                      @SerializedName("bounds")
                      val bounds: Bounds?,
                      @SerializedName("overview_polyline")
                      val overviewPolyline: OverviewPolyline
)