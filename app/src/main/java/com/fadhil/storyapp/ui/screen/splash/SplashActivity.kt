package com.fadhil.storyapp.ui.screen.splash

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fadhil.storyapp.databinding.ActivitySplashBinding
import com.fadhil.storyapp.ui.screen.home.HomeActivity
import com.fadhil.storyapp.ui.screen.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        lifecycleScope.launch {
            delay(1000)
            checkConfiguration()
        }
    }

    private fun checkConfiguration() {
        viewModel.getConfiguration().observe(this) {
            if (it?.isLogin == true) {
                HomeActivity.open(this)
                finish()
            } else {
                LoginActivity.open(this)
                finish()
            }
        }
    }

}