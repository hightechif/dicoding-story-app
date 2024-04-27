package com.fadhil.storyapp.ui.screen.add

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.fadhil.storyapp.R
import com.fadhil.storyapp.databinding.ActivityAddStoryBinding
import com.fadhil.storyapp.util.CameraUtils
import com.google.gson.Gson
import timber.log.Timber

class AddStoryActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_DATA = "data"

        fun open(originContext: FragmentActivity, data: Any? = null) {
            val intent = Intent(originContext, AddStoryActivity::class.java)
            val jsonOfData = Gson().toJson(data)
            if (data != null) intent.putExtra(EXTRA_DATA, jsonOfData)
            ActivityCompat.startActivity(originContext, intent, null)
        }
    }

    private lateinit var binding: ActivityAddStoryBinding
    private var currentImageUri: Uri? = null


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
        binding.btnCamera.setOnClickListener {
            startCamera()
        }
        binding.btnGallery.setOnClickListener {
            startGallery()
        }
        binding.btnUpload.setOnClickListener {

        }
    }

    private fun startCamera() {
        currentImageUri = CameraUtils.getImageUri(this)
        launcherIntentCamera.launch(currentImageUri)
    }

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { isSuccess ->
        if (isSuccess) {
            showImage()
        }
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

    private val launcherGallery = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Timber.d("Photo Picker - No media selected")
        }
    }


}