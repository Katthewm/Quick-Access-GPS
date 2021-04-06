package com.example.quickaccessgps.share

import com.google.firebase.firestore.DocumentId

data class Share(
    var name: String = "",
    var address: String = "",
    var from: String = ""
) {
    @DocumentId
    var id: String = ""
}
