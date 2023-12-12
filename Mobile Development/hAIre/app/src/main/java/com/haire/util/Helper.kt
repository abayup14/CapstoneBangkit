package com.haire.util

import android.content.Context
import android.view.View
import android.widget.Toast

class Helper {
    fun showLoading(isLoading: Boolean, progressBar: View) {
        if (isLoading) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    fun showText(context: Context, text: String) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show()
    }
}