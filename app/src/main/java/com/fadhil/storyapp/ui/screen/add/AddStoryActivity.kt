package com.fadhil.storyapp.ui.screen.add

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.FragmentActivity
import com.fadhil.storyapp.databinding.ActivityAddStoryBinding
import com.google.gson.Gson

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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}