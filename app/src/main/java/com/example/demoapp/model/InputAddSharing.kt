package com.example.demoapp.model

import java.io.File

data class InputAddSharing(
    val token: String? = null,
    val file: File? = null,
    val description: String? = null,
    val alamat: String? = null,
    val expire: String? = null,
    val contact: String? = null
)
