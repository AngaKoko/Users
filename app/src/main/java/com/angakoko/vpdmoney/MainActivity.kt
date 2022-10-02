package com.angakoko.vpdmoney

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.angakoko.vpdmoney.databinding.ActivityMainBinding
import com.angakoko.vpdmoney.main.MainViewModel
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val viewModelFactory = ViewModelFactory(this, application)
        viewModel = ViewModelProvider(this, viewModelFactory)[MainViewModel::class.java]

        binding.back.setOnClickListener {
            onBackPressed()
        }

        viewModel.getMessage().observe(this) {
            if (!TextUtils.isEmpty(it)) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_LONG).show()
                viewModel.setMessage("")
            }
        }

        binding.lifecycleOwner = this
        binding.viewModel = viewModel
    }
}