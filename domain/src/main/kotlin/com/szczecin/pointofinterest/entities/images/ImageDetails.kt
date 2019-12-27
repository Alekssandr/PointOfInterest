package com.szczecin.pointofinterest.entities.images

import com.google.gson.annotations.SerializedName

data class ImageDetails(

    @SerializedName("pageid") val pageId: Int,
    @SerializedName("ns") val ns: Int,
    @SerializedName("title") val title: String,
    @SerializedName("imagerepository") val imageRepository: String,
    @SerializedName("imageinfo") val imageInfo: List<ImageInfo>
)