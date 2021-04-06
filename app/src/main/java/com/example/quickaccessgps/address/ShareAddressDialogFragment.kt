package com.example.quickaccessgps.address

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import com.example.quickaccessgps.DataSingleton
import com.example.quickaccessgps.R

class ShareAddressDialogFragment : DialogFragment() {
    private lateinit var listener: ShareAddressDialogListener

    interface ShareAddressDialogListener {
        fun onDialogPositiveClick(dialog: DialogFragment, targetEmail: String, address: Address)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        try {
            listener = context as ShareAddressDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement ShareAddressDialogListener")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val addressId = arguments?.getString("addressId")
                ?: throw IllegalStateException("addressIndex cannot be null")
            val address = DataSingleton.addresses!!.find { address -> address.id == addressId }
                ?: throw IllegalStateException("address cannot be null")
            val view = it.layoutInflater.inflate(R.layout.share_address_dialog, null)
            val emailEditText: EditText = view.findViewById(R.id.share_email)

            val builder = AlertDialog.Builder(it)
                .setTitle("Share Address")
                .setPositiveButton("Send") { _, _ ->
                    listener.onDialogPositiveClick(
                        this,
                        emailEditText.text.toString(),
                        address
                    )
                }
                .setNegativeButton("Cancel") { _, _ -> }
                .setView(view)

            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }


}