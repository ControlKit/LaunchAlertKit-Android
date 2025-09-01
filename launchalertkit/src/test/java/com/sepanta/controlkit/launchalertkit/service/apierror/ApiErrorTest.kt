package com.sepanta.controlkit.launchalertkit.service.apierror

import com.sepanta.controlkit.launchalertkit.service.apiError.ApiError
import org.junit.Assert.assertEquals
import org.junit.Test

class ApiErrorTest {

    @Test
    fun `BAD_REQUEST should return Bad Request message`() {
        val error = ApiError(null, ApiError.ErrorStatus.BAD_REQUEST)
        assertEquals("Bad Request!", error.getErrorMessage())
    }

    @Test
    fun `FORBIDDEN should return Forbidden message`() {
        val error = ApiError(null, ApiError.ErrorStatus.FORBIDDEN)
        assertEquals("Forbidden!", error.getErrorMessage())
    }

    @Test
    fun `NOT_FOUND should return Not Found message`() {
        val error = ApiError(null, ApiError.ErrorStatus.NOT_FOUND)
        assertEquals("Not Found!", error.getErrorMessage())
    }

    @Test
    fun `METHOD_NOT_ALLOWED should return Method Not Allowed message`() {
        val error = ApiError(null, ApiError.ErrorStatus.METHOD_NOT_ALLOWED)
        assertEquals("Method Not Allowed!", error.getErrorMessage())
    }

    @Test
    fun `CONFLICT should return Conflict message`() {
        val error = ApiError(null, ApiError.ErrorStatus.CONFLICT)
        assertEquals("Conflict!", error.getErrorMessage())
    }

    @Test
    fun `UNAUTHORIZED should return Unauthorized message`() {
        val error = ApiError(null, ApiError.ErrorStatus.UNAUTHORIZED)
        assertEquals("Unauthorized!", error.getErrorMessage())
    }

    @Test
    fun `INTERNAL_SERVER_ERROR should return Internal Server error message`() {
        val error = ApiError(null, ApiError.ErrorStatus.INTERNAL_SERVER_ERROR)
        assertEquals("Internal Server error!", error.getErrorMessage())
    }

    @Test
    fun `NO_CONNECTION should return No Connection message`() {
        val error = ApiError(null, ApiError.ErrorStatus.NO_CONNECTION)
        assertEquals("No Connection!", error.getErrorMessage())
    }

    @Test
    fun `TIMEOUT should return Time Out message`() {
        val error = ApiError(null, ApiError.ErrorStatus.TIMEOUT)
        assertEquals("Time Out!", error.getErrorMessage())
    }

    @Test
    fun `UNKNOWN_ERROR should return Unknown Error message`() {
        val error = ApiError(null, ApiError.ErrorStatus.UNKNOWN_ERROR)
        assertEquals("Unknown Error!", error.getErrorMessage())
    }

    @Test
    fun `DATA_ERROR should return custom message`() {
        val error = ApiError("Custom error", ApiError.ErrorStatus.DATA_ERROR)
        assertEquals("Custom error", error.getErrorMessage())
    }

    @Test
    fun `constructor with message and status should set null code`() {
        val error = ApiError("Some message", ApiError.ErrorStatus.DATA_ERROR)
        assertEquals(null, error.code)
        assertEquals("Some message", error.message)
    }

    @Test
    fun `constructor with message code and status should keep code`() {
        val error = ApiError("Some message", 400, ApiError.ErrorStatus.BAD_REQUEST)
        assertEquals(400, error.code)
        assertEquals("Some message", error.message)
    }
}
