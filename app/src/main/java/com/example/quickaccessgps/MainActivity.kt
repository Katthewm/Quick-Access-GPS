package com.example.quickaccessgps

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), DeleteAddressDialogFragment.DeleteAddressDialogListener {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AddressRecyclerViewAdapter
    private lateinit var progressSpinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val addAddressButton: ImageButton = findViewById(R.id.add_address)
        addAddressButton.setOnClickListener { addAddress() }

        recyclerView = findViewById(R.id.address_rv)
        adapter = AddressRecyclerViewAdapter(
            { addressIndex -> selectAddress(addressIndex) },
            { addressIndex -> showDeleteDialog(addressIndex) }
        )
        recyclerView.adapter = adapter

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))
        progressSpinner = findViewById(R.id.progress_spinner)

        if (FirebaseAuth.getInstance().currentUser == null) {
            val providers = arrayListOf(
                AuthUI.IdpConfig.GoogleBuilder().build(),
                AuthUI.IdpConfig.EmailBuilder().build()
            )
            val intent =
                AuthUI.getInstance().createSignInIntentBuilder().setAvailableProviders(providers)
                    .build()

            val resultLauncher =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                    val response = IdpResponse.fromResultIntent(result.data)

                    if (result.resultCode == Activity.RESULT_OK) {
                        loadAddresses()
                    } else {
                        Log.e("MainActivity", "Login error: " + response?.error.toString())
                        finish()
                    }
                }
            resultLauncher.launch(intent)
        } else if (AddressSingleton.addresses == null || AddressSingleton.addresses!!.size == 0) {
            loadAddresses()
        }
    }

    private fun loadAddresses() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val db = Firebase.firestore
        progressSpinner.visibility = View.VISIBLE

        val userHashMap = hashMapOf(
            "email" to user.email
        )
        db.collection("users").document(user.uid).set(userHashMap, SetOptions.merge())
        db.collection("users").document(user.uid).collection("addresses").get()
            .addOnSuccessListener { querySnapshot ->
                AddressSingleton.addresses =
                    ArrayList(querySnapshot.documents.map { it.toObject<Address>()!! })
                adapter.notifyDataSetChanged()
                progressSpinner.visibility = View.GONE
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting addresses: ", it)
            }
    }

    private fun showDeleteDialog(addressIndex: Int) {
        val dialog = DeleteAddressDialogFragment().apply {
            arguments = Bundle().apply { putInt("addressIndex", addressIndex) }
        }

        dialog.show(supportFragmentManager, "DeleteAddressDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, addressIndex: Int) {
        AddressSingleton.removeAddress(addressIndex)
        adapter.notifyDataSetChanged()
    }

    fun selectAddress(addressIndex: Int) {
        val intent = Intent(this, AddressActivity::class.java).apply {
            putExtra(
                "addressIndex",
                addressIndex
            )
        }
        startActivity(intent)
    }

    private fun addAddress() {
        val intent = Intent(this, AddAddressActivity::class.java)
        startActivity(intent)
    }
}
