package com.fadhil.storyapp.ui.screen.home

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.ActivityCompat
import com.fadhil.storyapp.R
import com.fadhil.storyapp.databinding.ActivityHomeBinding
import com.fadhil.storyapp.ui.screen.login.LoginActivity
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObserver()
        setupListener()
    }

    private fun setupView() {

    }

    private fun setupObserver() {
        viewModel.getThemeSetting().observe(this) { isDarkMode ->
            if (isDarkMode) AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            else AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    private fun setupListener() {

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuLogout -> logout()
        }
        return false
    }

    fun logout() {
        viewModel.logout().observe(this) {
            if (it) LoginActivity.open(this)
        }
    }

    companion object {
        private const val EXTRA_DATA = "data"

        fun open(originContext: AppCompatActivity, data: Any? = null) {
            val intent = Intent(originContext, HomeActivity::class.java)
            val jsonOfData = Gson().toJson(data)
            if (data != null) intent.putExtra(EXTRA_DATA, jsonOfData)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ActivityCompat.startActivity(originContext, intent, null)
        }
    }

}