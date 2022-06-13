package com.example.demoapp.autentikasi.login

import androidx.core.util.PatternsCompat
import com.example.demoapp.R
import com.example.demoapp.menu.validation.InputValidation
import com.example.demoapp.model.InputLogin

class LoginValidation:InputValidation {
    override fun <T> validate(data: T, message: ((Int) -> Unit)?): Boolean {
        if (data !is InputLogin) return false

        val email = data.email

        if (email.isNullOrEmpty()) {
            message?.invoke(R.string.error_input_email)
            return false
        }

        if (PatternsCompat.EMAIL_ADDRESS.matcher(email).matches().not()) {
            message?.invoke(R.string.error_input_email)
            return false
        }

        val password = data.password

        if (password.isNullOrEmpty()) {
            message?.invoke(R.string.error_input_password)
            return false
        }

        if (password.length < 6) {
            message?.invoke(R.string.error_input_password)
            return false
        }

        return true
    }
}