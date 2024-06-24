package com.example.nearbyrestaurants.commonUtils.alertDialogs

import android.app.Dialog
import android.content.Context
import android.view.View
import android.view.LayoutInflater
import android.view.Gravity
import com.example.nearbyrestaurants.R


class ProgressBarDialog(context: Context?) : Dialog(context!!) {
    init {
        val wlmp = window!!.attributes
        wlmp.gravity = Gravity.CENTER_HORIZONTAL
        window!!.attributes = wlmp
        setTitle(null)
        setCancelable(false)
        setOnCancelListener(null)
        val view: View = LayoutInflater.from(context).inflate(
            R.layout.alert_dialog_for_progress_bar, null
        )
        setContentView(view)
    }
}