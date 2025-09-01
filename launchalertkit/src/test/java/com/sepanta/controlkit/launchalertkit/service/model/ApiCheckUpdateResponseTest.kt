package com.sepanta.controlkit.launchalertkit.service.model

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import java.util.Locale

class ApiCheckUpdateResponseTest {

    @Test
    fun `getContentBySystemLang should return content for system language`() {
        Locale.setDefault(Locale("fa"))
        val texts = listOf(
            LocalizedText("fa", "سلام"),
            LocalizedText("en", "Hello")
        )
        assertEquals("سلام", texts.getContentBySystemLang())
    }

    @Test
    fun `getContentBySystemLang should fallback to English`() {
        Locale.setDefault(Locale("de"))
        val texts = listOf(
            LocalizedText("fa", "سلام"),
            LocalizedText("en", "Hello")
        )
        assertEquals("Hello", texts.getContentBySystemLang())
    }

    @Test
    fun `getContentBySystemLang should return null if no match`() {
        Locale.setDefault(Locale("de"))
        val texts = listOf(
            LocalizedText("fa", "سلام"),
            LocalizedText("ar", "مرحبا")
        )
        assertNull(texts.getContentBySystemLang())
    }

    @Test
    fun `toDomain should map all fields correctly`() {
        Locale.setDefault(Locale("en"))
        val response = ApiCheckUpdateResponse(
            data = ApiData(
                id = "123",
                title = listOf(LocalizedText("en", "Update available")),
                description = listOf(LocalizedText("en", "Please update your app")),
                force = true,
                icon = "https://example.com/icon.png",
                link = "https://example.com/update",
                button_title = listOf(LocalizedText("en", "Update now")),
                cancel_button_title = listOf(LocalizedText("en", "Later")),
                version = listOf(LocalizedText("en", "1.2.3")),
                sdk_version = 30,
                minimum_version = "1.0.0",
                maximum_version = "2.0.0",
                created_at = "2025-01-01"
            )
        )

        val domain = response.toDomain()
        assertEquals("123", domain.id)
        assertEquals("1.2.3", domain.version)
        assertEquals("Update available", domain.title)
        assertEquals(true, domain.forceUpdate)
        assertEquals("Please update your app", domain.description)
        assertEquals("https://example.com/icon.png", domain.iconUrl)
        assertEquals("https://example.com/update", domain.linkUrl)
        assertEquals("Update now", domain.buttonTitle)
        assertEquals("Later", domain.cancelButtonTitle)
        assertEquals("30", domain.sdkVersion)
        assertEquals("1.0.0", domain.minimumVersion)
        assertEquals("2.0.0", domain.maximumVersion)
        assertEquals("2025-01-01", domain.created_at)
    }

    @Test
    fun `toDomain should handle nulls correctly`() {
        val response = ApiCheckUpdateResponse(
            data = ApiData(
                id = null,
                title = null,
                description = null,
                force = null,
                icon = null,
                link = null,
                button_title = null,
                cancel_button_title = null,
                version = null,
                sdk_version = null,
                minimum_version = null,
                maximum_version = null,
                created_at = null
            )
        )

        val domain = response.toDomain()
        assertNull(domain.id)
        assertNull(domain.version)
        assertNull(domain.title)
        assertNull(domain.forceUpdate)
        assertNull(domain.description)
        assertNull(domain.iconUrl)
        assertNull(domain.linkUrl)
        assertNull(domain.buttonTitle)
        assertNull(domain.cancelButtonTitle)
        assertNull(domain.sdkVersion)
        assertNull(domain.minimumVersion)
        assertNull(domain.maximumVersion)
        assertNull(domain.created_at)
    }
}
