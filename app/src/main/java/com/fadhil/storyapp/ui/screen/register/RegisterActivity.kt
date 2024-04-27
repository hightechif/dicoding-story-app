package com.fadhil.storyapp.ui.screen.register

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.isVisible
import com.fadhil.storyapp.data.ProcessResult
import com.fadhil.storyapp.data.ProcessResultDelegate
import com.fadhil.storyapp.data.source.remote.response.ApiResponse
import com.fadhil.storyapp.databinding.ActivityRegisterBinding
import com.fadhil.storyapp.util.StringUtil.Empty
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setupObserver()
        setupListener()
        loadData()
    }

    private fun setupView() {

    }

    private fun setupObserver() {
        viewModel.isFormValid.observe(this) {
            binding.btnRegister.isEnabled = it
        }
    }

    private fun setupListener() {
        binding.etName.addTextChangedListener(setTextWatcherCallback { s ->
            if (s != null) viewModel.setName(s)
        })
        binding.etEmail.addTextChangedListener(setTextWatcherCallback { s ->
            if (s != null) viewModel.setEmail(s)
        })
        binding.etPassword.addTextChangedListener(setTextWatcherCallback { s ->
            if (s != null) viewModel.setPassword(s)
        })

        binding.btnRegister.setOnClickListener {
            viewModel.register(
                viewModel.name.value!!,
                viewModel.email.value!!,
                viewModel.password.value!!
            ).observe(this) {
                ProcessResult(it, object : ProcessResultDelegate<ApiResponse<Any?>?> {
                    override fun loading() {
                        showLoadIndicator()
                    }

                    override fun error(code: String?, message: String?) {
                        hideLoadIndicator()
                        val snackBar = Snackbar.make(
                            binding.cvRegisterCard,
                            message ?: String.Empty,
                            Snackbar.LENGTH_SHORT
                        )
                        snackBar.show()
                    }

                    override fun unAuthorize(message: String?) {
                        hideLoadIndicator()
                        val snackBar = Snackbar.make(
                            binding.cvRegisterCard,
                            message ?: String.Empty,
                            Snackbar.LENGTH_SHORT
                        )
                        snackBar.show()
                    }

                    override fun success(data: ApiResponse<Any?>?) {
                        hideLoadIndicator()
                        if (data != null) {
                            intent.putExtra(EXTRA_REGISTRATION_STATUS, data.message)
                            setResult(RESULT_OK, intent)
                            finish()
                        }
                    }

                })
            }
        }
    }

    private fun loadData() {
        loadIntentData()
    }

    private fun loadIntentData() {
        val email = intent.getStringExtra(EXTRA_EMAIL)
        val password = intent.getStringExtra(EXTRA_PASSWORD)

        if (email?.isNotEmpty() == true) binding.etEmail.setText(email)
        if (password?.isNotEmpty() == true) binding.etPassword.setText(password)
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

    private fun showLoadIndicator() {
        binding.flProgress.isVisible = true
    }

    private fun hideLoadIndicator() {
        binding.flProgress.isVisible = false
    }

    companion object {
        private const val EXTRA_EMAIL = "email"
        private const val EXTRA_PASSWORD = "password"
        const val EXTRA_REGISTRATION_STATUS = "registration_status"

        fun open(
            originContext: AppCompatActivity,
            email: String?,
            password: String?,
            resultLauncher: ActivityResultLauncher<Intent>? = null
        ) {
            val intent = Intent(originContext, RegisterActivity::class.java)
            if (email?.isNotEmpty() == true) intent.putExtra(EXTRA_EMAIL, email)
            if (password?.isNotEmpty() == true) intent.putExtra(EXTRA_PASSWORD, password)
            if (resultLauncher == null) ActivityCompat.startActivity(originContext, intent, null)
            else resultLauncher.launch(intent)
        }
    }

}