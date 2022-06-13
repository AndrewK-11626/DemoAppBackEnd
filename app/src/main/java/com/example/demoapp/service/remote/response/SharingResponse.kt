package com.example.demoapp.service.remote.response

import com.google.gson.annotations.SerializedName

data class SharingResultResponse(
    @SerializedName(value = "id") val id: String? = null,
    @SerializedName(value = "name") val name: String? = null,
    @SerializedName(value = "description") val description: String? = null,
    @SerializedName(value = "alamat") val alamat: String? = null,
    @SerializedName(value = "expire") val expire: String? = null,
    @SerializedName(value = "contact") val contact: String? = null,
    @SerializedName(value = "photoUrl") val photoUrl: String? = null,
    @SerializedName(value = "createdAt") val createdAt: String? = null,
    @SerializedName(value = "lat") val latitude: String? = null,
    @SerializedName(value = "lon") val longitude: String? = null,
)

data class SharingResponse(
    @SerializedName(value = "listSharing") val listSharing: List<SharingResultResponse>? = null
) : BaseResponse()