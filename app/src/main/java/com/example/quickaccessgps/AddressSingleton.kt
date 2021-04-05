package com.example.quickaccessgps

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object AddressSingleton {
    public var addresses: ArrayList<Address>? = null
        set(value) {
            field = value; sortAddresses()
        }

    public fun addAddress(address: Address) {
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val addressHashMap = hashMapOf(
            "name" to address.name,
            "address" to address.address,
            "isFavorite" to address.isFavorite
        )
        db.collection("users").document(user.uid).collection("addresses").add(addressHashMap)
            .addOnSuccessListener { document ->
                address.id = document.id
            }
            .addOnFailureListener { e -> Log.e("TEST", "Failed to add: ", e) }
        addresses?.add(address)
        sortAddresses()
    }

    private fun sortAddresses() {
        addresses?.sortWith(Comparator { first, second ->
            if (first.isFavorite && !second.isFavorite) {
                return@Comparator -1
            } else if (!first.isFavorite && second.isFavorite) {
                return@Comparator 1
            } else {
                return@Comparator first.name.compareTo(second.name)
            }
        })
    }

    fun removeAddress(addressIndex: Int) {
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser ?: return
        addresses?.get(addressIndex)?.id?.let { id ->
            db.collection("users").document(user.uid).collection("addresses")
                .document(id).delete()
        }

        addresses?.removeAt(addressIndex)
    }

    fun setIsFavorite(addressIndex: Int, isFavorite: Boolean) {
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser ?: return
        addresses?.get(addressIndex)?.id?.let { id ->
            db.collection("users").document(user.uid).collection("addresses")
                .document(id).update(hashMapOf("isFavorite" to isFavorite) as Map<String, Any>)
        }

        addresses?.get(addressIndex)?.isFavorite = isFavorite
        sortAddresses()
    }
}