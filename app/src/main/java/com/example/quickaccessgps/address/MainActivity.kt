package com.example.quickaccessgps.address

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.quickaccessgps.DataSingleton
import com.example.quickaccessgps.R
import com.example.quickaccessgps.share.AcceptShareDialogFragment
import com.example.quickaccessgps.share.Share
import com.example.quickaccessgps.share.ShareRecyclerViewAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity(), DeleteAddressDialogFragment.DeleteAddressDialogListener,
    AcceptShareDialogFragment.AcceptShareDialogListener {
    private lateinit var addressRecyclerView: RecyclerView
    private lateinit var addressAdapter: AddressRecyclerViewAdapter
    private lateinit var shareRecyclerView: RecyclerView
    private lateinit var shareAdapter: ShareRecyclerViewAdapter
    private lateinit var progressSpinner: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val addAddressButton: ImageButton = findViewById(R.id.add_address)
        addAddressButton.setOnClickListener { addAddress() }

        addressRecyclerView = findViewById(R.id.address_rv)
        addressAdapter = AddressRecyclerViewAdapter(
            { addressIndex -> selectAddress(addressIndex) },
            { addressIndex -> showDeleteDialog(addressIndex) }
        )
        addressRecyclerView.adapter = addressAdapter
        addressRecyclerView.layoutManager = LinearLayoutManager(this)
        addressRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        shareRecyclerView = findViewById(R.id.share_rv)
        shareAdapter = ShareRecyclerViewAdapter {
            selectShare(it)
        }
        shareRecyclerView.adapter = shareAdapter
        shareRecyclerView.layoutManager = LinearLayoutManager(this).apply { reverseLayout = true }
        shareRecyclerView.addItemDecoration(
            DividerItemDecoration(
                this,
                DividerItemDecoration.VERTICAL
            )
        )

        progressSpinner = findViewById(R.id.progress_spinner)

        if (DataSingleton.addresses == null || DataSingleton.addresses!!.size == 0) {
            loadAddressesAndShares()
        }
    }

    private fun loadAddressesAndShares() {
        val user = FirebaseAuth.getInstance().currentUser ?: return
        val db = Firebase.firestore
        progressSpinner.visibility = View.VISIBLE

        val userHashMap = hashMapOf(
            "email" to user.email
        )
        db.collection("users").document(user.uid).set(userHashMap, SetOptions.merge())
        db.collection("users").document(user.uid).collection("addresses").get()
            .addOnSuccessListener { querySnapshot ->
                DataSingleton.addresses =
                    ArrayList(querySnapshot.documents.map { it.toObject<Address>()!! })
                addressAdapter.notifyDataSetChanged()
                progressSpinner.visibility = View.GONE
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting addresses: ", it)
            }
        db.collection("users").document(user.uid).collection("shares").get()
            .addOnSuccessListener { querySnapshot ->
                DataSingleton.shares =
                    ArrayList(querySnapshot.documents.map { it.toObject<Share>()!! })
                shareAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Log.e(TAG, "Error getting shares: ", it)
            }
    }

    private fun showDeleteDialog(addressIndex: Int) {
        val dialog = DeleteAddressDialogFragment().apply {
            arguments = Bundle().apply { putInt("addressIndex", addressIndex) }
        }

        dialog.show(supportFragmentManager, "DeleteAddressDialogFragment")
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, addressIndex: Int) {
        DataSingleton.removeAddress(addressIndex)
        addressAdapter.notifyDataSetChanged()
    }

    override fun onDialogPositiveClick(dialog: DialogFragment, share: Share) {
        DataSingleton.acceptShare(share)
        addressAdapter.notifyDataSetChanged()
        shareAdapter.notifyDataSetChanged()
    }

    override fun onDialogNegativeClick(dialog: DialogFragment, share: Share) {
        DataSingleton.removeShare(share)
        shareAdapter.notifyDataSetChanged()
    }

    fun selectAddress(addressIndex: Int) {
        val intent = Intent(this, AddressActivity::class.java).apply {
            putExtra("addressIndex", addressIndex)
        }
        startActivity(intent)
    }

    private fun selectShare(share: Share) {
        val dialog = AcceptShareDialogFragment().apply {
            arguments = Bundle().apply { putString("shareId", share.id) }
        }

        dialog.show(supportFragmentManager, "AcceptShareDialogFragment")
    }

    private fun addAddress() {
        val intent = Intent(this, AddAddressActivity::class.java)
        startActivity(intent)
    }
}
