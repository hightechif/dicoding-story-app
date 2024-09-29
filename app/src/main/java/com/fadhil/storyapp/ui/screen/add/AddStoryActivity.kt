package com.fadhil.storyapp.ui.screen.add

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.fadhil.storyapp.R
import com.fadhil.storyapp.data.ProcessResult
import com.fadhil.storyapp.data.ProcessResultDelegate
import com.fadhil.storyapp.data.Result
import com.fadhil.storyapp.data.source.remote.response.FileUploadResponse
import com.fadhil.storyapp.databinding.ActivityAddStoryBinding
import com.fadhil.storyapp.util.CameraUtils
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import timber.log.Timber


@AndroidEntryPoint
class AddStoryActivity : AppCompatActivity(), LocationListener {

    private lateinit var binding: ActivityAddStoryBinding
    private val viewModel: AddStoryViewModel by viewModels()
    private var currentImageUri: Uri? = null
    private var isGPSEnabled: Boolean = false
    private var isNetworkEnabled: Boolean = false
    private var isGPSTrackingEnabled: Boolean = false
    var location: Location? = null
    private lateinit var locationManager: LocationManager
    private lateinit var providerInfo: String

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            setResult(RESULT_CANCELED)
            finish()
        }
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            getLocation()
            showImage()
        } else {
            currentImageUri = null
        }
    }

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            getLocation()
            currentImageUri = uri
            showImage()
        } else {
            Timber.d("Photo Picker - No media selected")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObserver()
        setupListener()
    }

    private fun setupView() {

    }

    private fun setupObserver() {

    }

    private fun setupListener() {
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        getLocation()
        binding.btnCamera.setOnClickListener {
            startCamera()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.etDescription.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null) {
                    viewModel.setDescription(s.toString())
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.btnUpload.setOnClickListener {
            uploadStory()
        }
    }

    /**
     * Try to get my current location by GPS or Network Provider
     */
    @SuppressLint("MissingPermission")
    fun getLocation() {
        try {
            locationManager = getSystemService(LOCATION_SERVICE) as LocationManager

            //getting GPS status
            isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            //getting network status
            isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)

            // Try to get location if you GPS Service is enabled
            if (isGPSEnabled) {
                this.isGPSTrackingEnabled = true
                Timber.i("Application use GPS Service")

                /*
                 * This provider determines location using
                 * satellites. Depending on conditions, this provider may take a while to return
                 * a location fix.
                 */
                providerInfo = LocationManager.GPS_PROVIDER
            } else if (isNetworkEnabled) { // Try to get location if you Network Service is enabled
                this.isGPSTrackingEnabled = true

                Timber.i("Application use Network State to get GPS coordinates")

                /*
                 * This provider determines location based on
                 * availability of cell tower and WiFi access points. Results are retrieved
                 * by means of a network lookup.
                 */
                providerInfo = LocationManager.NETWORK_PROVIDER
            }


            // Application can use GPS or Network Provider
            if (providerInfo.isNotEmpty()) {
                locationManager.requestLocationUpdates(
                    providerInfo,
                    MIN_TIME_BW_UPDATES,
                    MIN_DISTANCE_CHANGE_FOR_UPDATES,
                    this
                )

                location = locationManager.getLastKnownLocation(providerInfo)
                updateGPSCoordinates()
            }
        } catch (e: Exception) {
            //e.printStackTrace();
            Timber.e("Impossible to connect to LocationManager", e)
        }
    }

    /**
     * Update GPSTracker latitude and longitude
     */
    private fun updateGPSCoordinates() {
        if (location != null) {
            val latLng = LatLng(location!!.latitude, location!!.longitude)
            viewModel.currentLatLng.postValue(latLng)
        }
    }

    private fun startCamera() {
        currentImageUri = CameraUtils.getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private fun showImage() {
        currentImageUri?.let {
            Timber.d("Image URI: - showImage: $it")
            Glide.with(this)
                .load(it)
                .placeholder(R.drawable.ic_placeholder)
                .into(binding.previewImageView)
        }
    }

    private fun startGallery() {
        launcherGallery.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private fun uploadStory() {
        if (currentImageUri == null) {
            Snackbar.make(
                binding.root,
                "Silakan ambil foto terlebih dahulu",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        if (viewModel.description.value?.isNotEmpty() != true) {
            Snackbar.make(
                binding.root,
                "Silakan isi deskripsi terelebih dahulu",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }
        lifecycleScope.launch {
            viewModel.addNewStory(
                this@AddStoryActivity,
                viewModel.description.value!!,
                currentImageUri!!,
                viewModel.currentLatLng.value?.latitude,
                viewModel.currentLatLng.value?.longitude
            ).collect {
                processUploadResponse(it)
            }
        }
    }

    private fun processUploadResponse(it: Result<FileUploadResponse?>) {
        ProcessResult(it, object : ProcessResultDelegate<FileUploadResponse?> {
            override fun loading() {

            }

            override fun error(code: String?, message: String?) {
                Timber.e("code=$code - message=$message")
                Snackbar.make(
                    binding.root,
                    "Upload process failed. $message",
                    Snackbar.LENGTH_SHORT
                ).show()
            }

            override fun unAuthorize(message: String?) {

            }

            override fun success(data: FileUploadResponse?) {
                setResult(RESULT_OK)
                finish()
            }

        })
    }

    override fun onLocationChanged(location: Location) {
        val latLng = LatLng(location.latitude, location.longitude)
        viewModel.currentLatLng.postValue(latLng)
    }

    companion object {
        private const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Float = 10F // 10 meters
        private const val MIN_TIME_BW_UPDATES: Long = (1000 * 60 * 1).toLong() // 1 minute

        fun open(
            originActivity: FragmentActivity,
            resultLauncher: ActivityResultLauncher<Intent>? = null
        ) {
            val intent = Intent(originActivity, AddStoryActivity::class.java)
            if (resultLauncher != null) {
                resultLauncher.launch(intent)
            } else {
                ActivityCompat.startActivity(originActivity, intent, null)
            }
        }
    }

}