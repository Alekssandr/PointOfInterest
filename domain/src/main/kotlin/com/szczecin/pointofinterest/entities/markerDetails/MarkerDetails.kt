package com.szczecin.pointofinterest.entities.markerDetails

import com.google.gson.annotations.SerializedName


data class MarkerDetails(

    @SerializedName("pageid") val pageid: Int,
    @SerializedName("ns") val ns: Int,
    @SerializedName("title") val title: String,
    @SerializedName("contentmodel") val contentmodel: String,
    @SerializedName("pagelanguage") val pagelanguage: String,
    @SerializedName("pagelanguagehtmlcode") val pagelanguagehtmlcode: String,
    @SerializedName("pagelanguagedir") val pagelanguagedir: String,
    @SerializedName("touched") val touched: String,
    @SerializedName("lastrevid") val lastrevid: Int,
    @SerializedName("length") val length: Int,
    @SerializedName("description") val description: String,
    @SerializedName("descriptionsource") val descriptionsource: String,
    @SerializedName("images") val images: List<Images>
)