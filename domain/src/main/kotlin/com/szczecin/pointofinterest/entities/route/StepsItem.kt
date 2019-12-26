package com.szczecin.pointofinterest.entities.route

import com.google.gson.annotations.SerializedName
import com.szczecin.pointofinterest.entities.route.*

data class StepsItem(@SerializedName("duration")
                     val duration: Duration,
                     @SerializedName("start_location")
                     val startLocation: StartLocation,
                     @SerializedName("distance")
                     val distance: Distance,
                     @SerializedName("travel_mode")
                     val travelMode: String = "",
                     @SerializedName("html_instructions")
                     val htmlInstructions: String = "",
                     @SerializedName("end_location")
                     val endLocation: EndLocation,
                     @SerializedName("polyline")
                     val polyline: Polyline
)