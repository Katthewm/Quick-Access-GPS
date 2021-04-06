package com.example.quickaccessgps

import android.util.Log
import com.example.quickaccessgps.address.Address
import com.example.quickaccessgps.share.Share
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object DataSingleton {
    var addresses: ArrayList<Address>? = null
        set(value) {
            field = value; sortAddresses()
        }

    var shares: ArrayList<Share> = ArrayList()

    fun addAddress(address: Address) {
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
            .addOnFailureListener { e -> Log.e("AddressSingleton", "Failed to add: ", e) }
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

    fun sendShare(targetEmail: String, address: Address): Task<QuerySnapshot>? {
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser ?: return null
        val share = hashMapOf(
            "name" to address.name,
            "address" to address.address,
            "from" to user.email
        )

        return db.collection("users").whereEqualTo("email", targetEmail).get()
            .addOnSuccessListener {
                val targetId = it.documents[0].id
                db.collection("users").document(targetId).collection("shares").add(share)
            }
            .addOnFailureListener { e -> Log.e("AddressSingleton", "Failed to share: ", e) }
    }

    fun removeShare(share: Share) {
        val db = Firebase.firestore
        val user = FirebaseAuth.getInstance().currentUser ?: return

        db.collection("users").document(user.uid).collection("shares")
            .document(share.id).delete()
        shares.remove(share)
    }

    fun acceptShare(share: Share) {
        val address = Address(share.name, share.address, false)
        removeShare(share)
        addAddress(address)
    }
}