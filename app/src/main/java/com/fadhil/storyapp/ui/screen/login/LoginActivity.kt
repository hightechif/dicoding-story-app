package com.fadhil.storyapp.ui.screen.login

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.fadhil.storyapp.data.ProcessResult
import com.fadhil.storyapp.data.ProcessResultDelegate
import com.fadhil.storyapp.data.source.remote.response.ResLogin
import com.fadhil.storyapp.databinding.ActivityLoginBinding
import com.fadhil.storyapp.ui.screen.home.HomeActivity
import com.fadhil.storyapp.ui.screen.register.RegisterActivity
import com.fadhil.storyapp.util.StringUtil.Empty
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    companion object {
        private const val EXTRA_DATA = "data"

        fun open(originContext: AppCompatActivity, data: Any? = null) {
            val intent = Intent(originContext, LoginActivity::class.java)
            val jsonOfData = Gson().toJson(data)
            if (data != null) intent.putExtra(EXTRA_DATA, jsonOfData)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or
                    Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
            ActivityCompat.startActivity(originContext, intent, null)
        }
    }

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    private val registerResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_OK) {
                val intent = it.data
                val message = intent?.getStringExtra(RegisterActivity.EXTRA_REGISTRATION_STATUS)
                val snackBar =
                    Snackbar.make(binding.root, message ?: String.Empty, Snackbar.LENGTH_SHORT)
                snackBar.show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObserver()
        setupListener()
    }

    private fun setupView() {

    }

    private fun setupObserver() {
        viewModel.isFormValid.observe(this) {
            binding.btnLogin.isEnabled = it
        }
    }

    private fun setupListener() {
        binding.etEmail.addTextChangedListener(setTextWatcherCallback { s ->
            viewModel.setEmail(s ?: String.Empty)
        })
        binding.etPassword.addTextChangedListener(setTextWatcherCallback { s ->
            viewModel.setPassword(s ?: String.Empty)
        })

        binding.tvRegister.setOnClickListener {
            RegisterActivity.open(
                this,
                viewModel.email.value,
                viewModel.password.value,
                registerResultLauncher
            )
        }

        binding.btnLogin.setOnClickListener {
            login()
        }
    }

    private fun setTextWatcherCallback(callback: (s: String?) -> Unit): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // do nothing
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                callback.invoke(s?.toString())
            }

            override fun afterTextChanged(s: Editable?) {
                // do nothing
            }

        }
    }

    private fun login() {
        viewModel.login(viewModel.email.value!!, viewModel.password.value!!).observe(this) {
            ProcessResult(it, object : ProcessResultDelegate<ResLogin?> {
                override fun loading() {
                    showLoadIndicator()
                }

                override fun error(code: String?, message: String?) {
                    hideLoadIndicator()
                    val snackBar = Snackbar.make(
                        binding.cvLoginCard,
                        message ?: String.Empty,
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                }

                override fun unAuthorize(message: String?) {
                    hideLoadIndicator()
                    val snackBar = Snackbar.make(
                        binding.cvLoginCard,
                        message ?: String.Empty,
                        Snackbar.LENGTH_SHORT
                    )
                    snackBar.show()
                }

                override fun success(data: ResLogin?) {
                    hideLoadIndicator()
                    if (data != null) {
                        HomeActivity.open(this@LoginActivity)
                    }
                }

            })
        }
    }

    private fun showLoadIndicator() {
        binding.flProgress.isVisible = true
    }

    private fun hideLoadIndicator() {
        binding.flProgress.isVisible = false
    }

}