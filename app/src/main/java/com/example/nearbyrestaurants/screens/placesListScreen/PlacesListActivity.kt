package com.example.nearbyrestaurants.screens.placesListScreen

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsetsController
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.demo.moviesapp.screens.popularAndTopRatedList.adapter.PlacesListAdapter
import com.example.nearbyrestaurants.App
import com.example.nearbyrestaurants.R
import com.example.nearbyrestaurants.commonUtils.Constants
import com.example.nearbyrestaurants.commonUtils.IntentConstants
import com.example.nearbyrestaurants.commonUtils.ResponseType
import com.example.nearbyrestaurants.commonUtils.alertDialogs.AlertDialogForErrorMessage
import com.example.nearbyrestaurants.commonUtils.alertDialogs.AlertDialogForNoInternetConnectionMessage
import com.example.nearbyrestaurants.commonUtils.alertDialogs.ProgressBarDialog
import com.example.nearbyrestaurants.commonUtils.commonModels.Result
import com.example.nearbyrestaurants.databinding.ActivityPlacesListBinding
import com.example.nearbyrestaurants.screens.placeDetailsScreen.PlaceDetailsActivity
import com.example.nearbyrestaurants.screens.placesListScreen.viewModels.PlacesListActivityViewModel
import com.example.nearbyrestaurants.screens.placesListScreen.viewModels.PlacesListActivityViewModelFactory
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.gson.Gson
import javax.inject.Inject


class PlacesListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlacesListBinding
    private lateinit var progressBarDialog: ProgressBarDialog
    private lateinit var placesListAdapter: PlacesListAdapter
    private lateinit var placesListActivityViewModel: PlacesListActivityViewModel

    @Inject
    lateinit var placesListActivityViewModelFactory: PlacesListActivityViewModelFactory

    private val placesList: ArrayList<com.example.nearbyrestaurants.commonUtils.commonModels.Result> = ArrayList<com.example.nearbyrestaurants.commonUtils.commonModels.Result>()

    private val LOCATION_PERMISSION_REQUEST_CODE = 1

    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private var lattLong: String = ""

    private var latitude: Double = 0.0
    private var longitude: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_places_list)

        progressBarDialog = ProgressBarDialog(context = this)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        checkLocationPermission()

        listeners()
    }

    /////////////////////////////////////////////////////////////
    // Initial Methods
    ////////////////////////////////////////////////////////////

    fun checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE)
        } else {
            showProgressBar()
            getCurrentLocation()
        }
    }

    private fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            dismissProgressBar()
            binding.tvAllowLocationPermission.visibility = View.VISIBLE
            Toast.makeText(this, getString(R.string.to_see_nearby_restaurants_you_need), Toast.LENGTH_SHORT).show()
            return
        }

        binding.tvAllowLocationPermission.visibility = View.GONE

        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    latitude = location.latitude
                    longitude = location.longitude
                    // Use latitude and longitude as needed
                    lattLong = "$latitude,$longitude"

                    if(!lattLong.isNullOrEmpty()) {
                        init()
                    }

                    Toast.makeText(this, "Lat: $latitude, Long: $longitude", Toast.LENGTH_LONG).show()
                } else {
                    dismissProgressBar()
                    Toast.makeText(this, "Location not available, Please turn on device location", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun init() {

        initRecyclerView()

        (this?.application as App).applicationComponent.inject(this)

        placesListActivityViewModel = ViewModelProvider(this, placesListActivityViewModelFactory).get(PlacesListActivityViewModel::class.java)

        showProgressBar()

        placesListActivityViewModel.getPlacesList(lattLong, 13199)

        observePlacesDataChanges()

    }

    private fun listeners() {
        binding.ivBackIcon.setOnClickListener {
            finish()
        }

        binding.ivRefreshIcon.setOnClickListener() {
            showProgressBar()
            getCurrentLocation()
        }
    }


    private fun initRecyclerView() {
        placesListAdapter = PlacesListAdapter(
            this,
                placesList.toMutableList(),
            object : PlacesListAdapter.OnOptionClickListener {
                override fun onPlaceItemClick(result: com.example.nearbyrestaurants.commonUtils.commonModels.Result) {
                    var intent = Intent(this@PlacesListActivity, PlaceDetailsActivity::class.java)
                    intent.putExtra(IntentConstants.FSQ_ID, result.fsq_id)
                    intent.putExtra(IntentConstants.LATTITUDE, latitude)
                    intent.putExtra(IntentConstants.LONGITUDE, longitude)
                    startActivity(intent)
                }
            })

        val linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.setLayoutManager(linearLayoutManager)

        /*     code can be used for pagination, but this api doesn't support pagination

               val scrollListener: EndlessRecyclerViewScrollListener =
                   object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
                       override fun onLoadMore(page: Int) {
                           showProgressBar()
                           placesListActivityViewModel.getPlacesList("30.71,76.71", 13199)
                       }
                   }

               binding.recyclerView.addOnScrollListener(scrollListener) */

        binding.recyclerView.setAdapter(placesListAdapter)
    }



    /////////////////////////////////////////////////////////
    // Observe data changes
    ////////////////////////////////////////////////////////

    private fun observePlacesDataChanges() {

        placesListActivityViewModel.placesList.observe(this, Observer {
            dismissProgressBar()
            when (it) {
                is ResponseType.Loading -> {

                }
                is ResponseType.Success -> {

                    var places: MutableList<Result> = mutableListOf()

                    it.data?.let { it ->
                        placesListAdapter.setPlacesList(it.toMutableList())
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


    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                showProgressBar()
                getCurrentLocation()
            } else {
                // Permission denied
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
            }
        }
    }


}