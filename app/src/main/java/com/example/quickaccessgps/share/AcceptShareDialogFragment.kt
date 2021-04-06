package com.example.quickaccessgps.share

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.quickaccessgps.DataSingleton

class AcceptShareDialogFragment : DialogFragment() {
    private lateinit var listener: AcceptShareDialogListener

    interface AcceptShareDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, share: Share)
        fun onDialogNegativeClick(dialog: DialogFragment, share: Share)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as AcceptShareDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement AcceptShareDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val shareId = arguments?.getString("shareId")
                ?: throw IllegalStateException("shareId cannot be null")
            val share = DataSingleton.shares.find { share -> share.id == shareId }
                ?: throw IllegalStateException("share cannot be null")
            val builder = AlertDialog.Builder(it)
                .setTitle("Accept Share?")
                .setMessage(
                    "Do you accept ${share.name}: ${share.address}?"
                )
                .setPositiveButton("Accept") { _, _ ->
                    listener.onDialogPositiveClick(this, share)
                }
                .setNegativeButton("Deny") { _, _ ->
                    listener.onDialogNegativeClick(this, share)
                }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}