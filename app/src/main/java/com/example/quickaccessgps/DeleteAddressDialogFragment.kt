package com.example.quickaccessgps

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class DeleteAddressDialogFragment : DialogFragment() {

    private lateinit var listener: DeleteAddressDialogListener

    interface DeleteAddressDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, addressIndex: Int)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as DeleteAddressDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement DeleteAddressDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val addressIndex = arguments?.getInt("addressIndex")
                ?: throw IllegalStateException("addressIndex cannot be null")
            val builder = AlertDialog.Builder(it)
                .setTitle("Delete Address")
                .setMessage("Are you sure that you want to delete ${AddressSingleton.addresses[addressIndex].name}?")
                .setPositiveButton("Delete") { _, _ ->
                    listener.onDialogPositiveClick(
                        this,
                        addressIndex
                    )
                }
                .setNegativeButton("Cancel") { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}