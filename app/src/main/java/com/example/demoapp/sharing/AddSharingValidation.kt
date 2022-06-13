package com.example.demoapp.sharing

import com.example.demoapp.R
import com.example.demoapp.menu.validation.InputValidation
import com.example.demoapp.model.InputAddSharing

class AddSharingValidation: InputValidation {
    override fun <T> validate(data: T, message: ((Int) -> Unit)?): Boolean {
        if (data !is InputAddSharing) return false

        val token = data.token

        if (token.isNullOrEmpty()) {
            message?.invoke(R.string.error_token)
            return false
        }

        val file = data.file

        if (file == null) {
            message?.invoke(R.string.error_null_file)
            return false
        }

        val desc = data.description

        return true
    }
}