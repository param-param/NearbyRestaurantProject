package com.example.nearbyrestaurants.screens.placeDetailsScreen

import android.content.Intent
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.nearbyrestaurants.App
import com.example.nearbyrestaurants.R
import com.example.nearbyrestaurants.commonUtils.Constants
import com.example.nearbyrestaurants.commonUtils.IntentConstants
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.alertDialogs.AlertDialogForErrorMessage
import com.example.nearbyrestaurants.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.example.nearbyrestaurants.commonUtils.alertDialogs.ProgressBarDialog
import com.example.nearbyrestaurants.databinding.ActivityPlaceDetailsBinding
import com.example.nearbyrestaurants.screens.placeDetailsScreen.viewModels.PlaceDetailsActivityViewModel
import com.example.nearbyrestaurants.screens.placeDetailsScreen.viewModels.PlaceDetailsActivityViewModelFactory
import com.google.gson.Gson
import java.util.Locale
import javax.inject.Inject


class PlaceDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailsBinding
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var placeDetailsActivityViewModel: PlaceDetailsActivityViewModel

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var destination: String = ""

    @Inject
    lateinit var placeDetailsActivityViewModelFactory: PlaceDetailsActivityViewModelFactory

    var fsqId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
//
        binding = DataBindingUtil.setContentView(this, R.layout.activity_place_details)


        if (intent == null) {
            finish()
        }

        fsqId = intent.getStringExtra(IntentConstants.FSQ_ID).toString()
        latitude = intent.getDoubleExtra(IntentConstants.LATTITUDE, 0.0)
        longitude = intent.getDoubleExtra(IntentConstants.LONGITUDE, 0.0)

        if(fsqId.isNullOrEmpty()) {
            finish()
        }

        init()
        listeners()
    }

    /////////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////////

    private fun init() {
        progressBarDialog = ProgressBarDialog(context = this)

        (this?.application as App).applicationComponent.inject(this)

        placeDetailsActivityViewModel = ViewModelProvider(this, placeDetailsActivityViewModelFactory).get(
            PlaceDetailsActivityViewModel::class.java)

        showProgressBar()

        placeDetailsActivityViewModel.getPlaceDetails(fsqId)
        placeDetailsActivityViewModel.getPlacePhotos(fsqId)

        observePlacesDetailDataChanges()
        observePlacesPhotosDataChanges()

    }

    private fun listeners() {
        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.btnStartNavigation.setOnClickListener() {
            openGoogleMapsForNavigation(latitude, longitude)
        }
    }

    private fun openGoogleMapsForNavigation(lat: Double, lng: Double) {

        var source = convertLattLongToAddress()

        if(!source.isNullOrEmpty() && !destination.isNullOrEmpty()) {
            val uri = Uri.parse("https://www.google.com/maps/dir/$source/$destination")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")

            if (intent.resolveActivity(packageManager) != null) {
                startActivity(intent)
            } else {
                // Fallback: Open in browser if Google Maps app is not installed
                val browserIntent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(browserIntent)
            }

        }
    }

    private fun convertLattLongToAddress() : String{
        try {
            val geocoder = Geocoder(this, Locale.getDefault())

            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            val address = addresses!![0].getAddressLine(0)
            return address
        } catch (e: Exception) {
            e.printStackTrace()
            return ""
        }
    }


    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observePlacesDetailDataChanges() {

        placeDetailsActivityViewModel.placeDetails.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is ResponseType.Loading -> {

                }
                is ResponseType.Success -> {

                    it.data?.let { it ->
                        binding.tvRestaurantName.text = it.name

                        if(it.location.formatted_address.isNullOrEmpty()) {
                            binding.tvAddress.text = getString(R.string.not_available)
                        } else {
                            destination = it.location.formatted_address
                            binding.tvAddress.text = it.location.formatted_address
                        }

                        if(it.closed_bucket.contentEquals(Constants.LIKELY_OPEN, true)) {
                            binding.tvStatus.text = "OPEN"
                        } else {
                            binding.tvStatus.text = "NA"
                        }
                    }

                }
                is ResponseType.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(supportFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMessage()
                        dialog.show(supportFragmentManager, "dialog")
                    }
                }
            }
        })
    }

    private fun observePlacesPhotosDataChanges() {

        placeDetailsActivityViewModel.placePhotos.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is ResponseType.Loading -> {

                }
                is ResponseType.Success -> {

                    it.data?.let { it ->
                        if(it.isNotEmpty()){
                            Glide.with(this)
                                .load(it[0].getPhoto())
                                .error(R.drawable.baseline_no_image)
                                .placeholder(R.drawable.baseline_no_image)
                                .into(binding.ivPoster)

                        }
                    }

                }
                is ResponseType.Error -> {
                    var errorMsg = Gson().toJson(it)
                    if (errorMsg.contains(Constants.NO_INTERNET_CONNECTION)) {

                        val dialog: DialogFragment = AlertDialogForNoInternetConnectionMessage()
                        dialog.show(supportFragmentManager, "dialog")

                    } else {
                        val dialog: DialogFragment = AlertDialogForErrorMessage()
                        dialog.show(supportFragmentManager, "dialog")
                    }
                }
            }
        })
    }

    //////////////////////////////////////////////////////////
    // ProgressBar
    //////////////////////////////////////////////////////////
    private fun showProgressBar() {
        try {
            if (!this.isFinishing) {
                if (progressBarDialog != null) {
                    progressBarDialog.show()
                }
            }
        } catch (ignore: Exception) {
        }
    }


    private fun dismissProgressBar() {
        try {
            if (!this.isFinishing) {
                if (progressBarDialog != null) {
                    progressBarDialog.dismiss()
                }
            }
        } catch (ignore: Exception) {
        }
    }


}