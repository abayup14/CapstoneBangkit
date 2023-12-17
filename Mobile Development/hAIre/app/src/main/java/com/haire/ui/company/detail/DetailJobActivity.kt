package com.haire.ui.company.detail

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.haire.R
import com.haire.databinding.ActivityDetailJobBinding

class DetailJobActivity : AppCompatActivity() {
    private var _binding: ActivityDetailJobBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDetailJobBinding.inflate(layoutInflater)
        setContentView(binding.root)


    }

    companion object {
        const val EXTRA_ID = "extra_id"
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}