package com.example.demoapp.menu.validation

interface InputValidation {
    fun <T> validate(data: T, message: ((Int) -> Unit)? = null): Boolean
}