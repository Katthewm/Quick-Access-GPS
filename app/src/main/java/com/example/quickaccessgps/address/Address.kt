package com.example.quickaccessgps.address

import com.google.firebase.firestore.DocumentId

data class Address(
    var name: String = "",
    var address: String = "",
    @field:JvmField var isFavorite: Boolean = false
) {
    @DocumentId
    var id: String = ""
}