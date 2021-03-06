package com.example.demoapp.utils

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.core.view.children
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import coil.load
import com.example.demoapp.DemoApp
import com.example.demoapp.R
import java.util.*

fun Fragment.injector() = (requireActivity().applicationContext as DemoApp).injector

fun Fragment.disableUserInteraction() {
    requireActivity().window.setFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun Fragment.enableUserInteraction() {
    requireActivity().window.clearFlags(
        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
    )
}

fun View.hideKeyboard() {
    val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

fun ViewGroup.clearAllFocus() {
    children.forEach {
        it.clearFocus()
    }
}

fun String.formatToken(): String {
    return "Bearer $this"
}

fun String.formatDate(): String {
    val parseFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS", Locale.getDefault())
    val date = parseFormat.parse(this) ?: Date()
    val dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault())
    return dateFormat.format(date)
}

fun ImageView.loadImage(url: String?) {
    load(url) {
        crossfade(true)
        placeholder(R.color.teal_200)
        error(R.drawable.ic_baseline_phone_24)
    }
}