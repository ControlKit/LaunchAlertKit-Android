package com.sepanta.controlkit.launchalertkit.view.viewmodel

import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.apiError.NetworkResult
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import com.sepanta.controlkit.launchalertkit.service.model.ActionResponse
import com.sepanta.controlkit.launchalertkit.service.model.ApiCheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.service.model.ApiData
import com.sepanta.controlkit.launchalertkit.service.model.LocalizedText
import com.sepanta.controlkit.launchalertkit.view.viewmodel.state.LaunchAlertState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

class LaunchAlertViewModelSimpleTest {

    private val mockApi: LaunchAlertApi = mockk(relaxed = true)
    private val mockLocalDataSource: LocalDataSource = mockk(relaxed = true)
    private lateinit var viewModel: LaunchAlertViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = LaunchAlertViewModel(mockApi, mockLocalDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test getData with valid response sets ShowView state`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = "00000000-0000-0000-0000-000000000002",
            title = listOf(localizedText),
            description = listOf(localizedText),
            force = false,
            icon = "https://test.com/image.jpg",
            link = "https://test.com",
            button_title = listOf(localizedText),
            cancel_button_title = listOf(localizedText),
            version = listOf(localizedText),
            sdk_version = 1,
            minimum_version = "1.0.0",
            maximum_version = "2.0.0",
            created_at = "2023-01-01"
        )
        val response = ApiCheckUpdateResponse(apiData)

        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(response)
        coEvery { mockApi.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(ActionResponse())
        coEvery { mockLocalDataSource.saveLastId(any()) } returns Unit
        coEvery { mockLocalDataSource.getLastId() } returns "00000000-0000-0000-0000-000000000001"

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        println("DEBUG: Current state = $state")
        // After getData, state should be Action because sendAction is called automatically
        assertTrue("Expected Action but got $state", state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)

        // Verify API was called
        coVerify { 
            mockApi.getLaunchAlertData(
                any(),
                "test_app",
                "1.0.0", 
                "test_device",
                "00000000-0000-0000-0000-000000000001"
            ) 
        }

        // Verify lastId was saved
        coVerify { mockLocalDataSource.saveLastId("00000000-0000-0000-0000-000000000002") }
    }

    @Test
    fun `test getData with null response sets NoUpdate state`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)
        coEvery { mockLocalDataSource.getLastId() } returns "00000000-0000-0000-0000-000000000001"

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.NoAlert)
    }

    @Test
    fun `test multiple getData calls work correctly`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = "00000000-0000-0000-0000-000000000002",
            title = listOf(localizedText),
            description = listOf(localizedText),
            force = false,
            icon = "https://test.com/image.jpg",
            link = "https://test.com",
            button_title = listOf(localizedText),
            cancel_button_title = listOf(localizedText),
            version = listOf(localizedText),
            sdk_version = 1,
            minimum_version = "1.0.0",
            maximum_version = "2.0.0",
            created_at = "2023-01-01"
        )
        val response1 = ApiCheckUpdateResponse(apiData)

        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(response1) andThen NetworkResult.Success(null)
        coEvery { mockApi.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Success(ActionResponse())
        coEvery { mockLocalDataSource.saveLastId(any()) } returns Unit
        coEvery { mockLocalDataSource.getLastId() } returns "00000000-0000-0000-0000-000000000001"

        // When - First call
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - First response
        var state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        assertEquals("VIEW", (state as LaunchAlertState.Action).data)

        // When - Second call
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Second response
        state = viewModel.state.value
        assertTrue(state is LaunchAlertState.NoAlert)
    }
}
