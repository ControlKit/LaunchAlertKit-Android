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

class LaunchAlertViewModelIntegrationSimpleTest {

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
    fun `test complete user flow from initialization to action completion`() = runTest {
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

        // When - Step 1: Initialize and get data
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Step 1: Should be in Action state after getData
        var state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)

        // When - Step 2: User accepts
        viewModel.submitDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Step 2: Should be in Action state with ACCEPTED
        state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        val acceptedState = state as LaunchAlertState.Action
        assertEquals("ACCEPTED", acceptedState.data)

        // Verify API calls
        coVerify { 
            mockApi.getLaunchAlertData(
                any(),
                "test_app",
                "1.0.0", 
                "test_device",
                "00000000-0000-0000-0000-000000000001"
            ) 
        }
        
        coVerify { mockLocalDataSource.saveLastId("00000000-0000-0000-0000-000000000002") }
    }

    @Test
    fun `test complete user flow with cancellation`() = runTest {
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

        // When - Step 1: Initialize and get data
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Step 1: Should be in Action state after getData
        var state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)

        // When - Step 2: User cancels
        viewModel.dismissDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Step 2: Should be in Action state with CANCELED
        state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        val canceledState = state as LaunchAlertState.Action
        assertEquals("CANCELED", canceledState.data)
    }

    @Test
    fun `test error recovery flow`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // First call - error
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(mockk())
        coEvery { mockLocalDataSource.getLastId() } returns "00000000-0000-0000-0000-000000000001"

        // When - First call fails
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be in ShowViewError state
        var state = viewModel.state.value
        assertTrue(state is LaunchAlertState.ShowViewError)

        // Second call - success
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

        // When - Second call succeeds
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be in Action state
        state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)
    }
}
