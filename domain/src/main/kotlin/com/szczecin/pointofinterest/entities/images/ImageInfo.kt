package com.szczecin.pointofinterest.entities.images

import com.google.gson.annotations.SerializedName

data class ImageInfo(

    @SerializedName("url") val url: String,
    @SerializedName("descriptionurl") val descriptionUrl: String,
    @SerializedName("descriptionshorturl") val descriptionshortUrl: String,
    @SerializedName("thumburl") val thumburl: String
)