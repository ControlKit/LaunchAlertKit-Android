package com.sepanta.controlkit.launchalertkit.service.apiError.model

import com.sepanta.errorhandler.IErrorEntity

/*
 *  File: Error422Validation.kt
 *
 *  Created by morteza on 9/9/25.
 */
data class ErrorValidation(
    override val message: String,
    val success: Boolean,
    val error_code: String,
    val errors: ValidationErrors? = null,
) : IErrorEntity

data class ValidationErrors(
    val email: List<String>? = null,
    val subject: List<String>? = null,
    val message: List<String>? = null
)
