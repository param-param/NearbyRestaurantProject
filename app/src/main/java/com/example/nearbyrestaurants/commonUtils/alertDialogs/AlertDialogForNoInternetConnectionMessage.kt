package com.example.nearbyrestaurants.commonUtils.alertDialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.Window
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import com.example.nearbyrestaurants.R

class AlertDialogForNoInternetConnectionMessage
    : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val view: View = requireActivity()!!.layoutInflater.inflate(
            R.layout.alert_dialog_for_no_internet_connection_message, LinearLayout(
                activity
            ), false
        )

        val continueBtn = view.findViewById<View>(R.id.btn_Continue) as AppCompatButton
        val crossBtn = view.findViewById<View>(R.id.iv_Close_BTN) as ImageView


        continueBtn.setOnClickListener{
            dismiss()
        }

        crossBtn.setOnClickListener{
            dismiss()
        }

        // Build dialog
        val builder = Dialog(requireActivity()!!)
        builder.requestWindowFeature(Window.FEATURE_NO_TITLE)
        builder.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        builder.setContentView(view)
        return builder
    }
}
