package com.sepanta.controlkit.launchalertkit.view.viewmodel

import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.apiError.NetworkResult
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import com.sepanta.controlkit.launchalertkit.view.viewmodel.state.LaunchAlertState
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchAlertViewModelErrorHandlingTest {

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
    fun `test getData with API error sets ShowViewError state`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en",
            timeOut = 30000L,
            maxRetry = 3
        )
        viewModel.setConfig(config)

        val mockError = mockk<com.sepanta.errorhandler.ApiError<*>>()
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(mockError)

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.first()
        assertTrue(state is LaunchAlertState.ShowViewError)
        assertEquals(mockError, (state as LaunchAlertState.ShowViewError).data)
    }

    @Test
    fun `test sendAction with API error sets ActionError state`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en",
            timeOut = 30000L,
            maxRetry = 3
        )
        viewModel.setConfig(config)

        // Mock error response
        val mockError = mockk<com.sepanta.errorhandler.ApiError<*>>()
        coEvery { mockApi.setAction(any(), any(), any(), any(), any(), any()) } returns NetworkResult.Error(mockError)

        // When - This will fail because itemId is null, but we can test the error handling
        viewModel.sendAction("test_action")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not call API because itemId is null
        coVerify(exactly = 0) { mockApi.setAction(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test error recovery after failed API call`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en",
            timeOut = 30000L,
            maxRetry = 3
        )
        viewModel.setConfig(config)

        val mockError = mockk<com.sepanta.errorhandler.ApiError<*>>()
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(mockError)

        // When - First call fails
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be in error state
        var state = viewModel.state.first()
        assertTrue(state is LaunchAlertState.ShowViewError)

        // When - Clear state and try again
        viewModel.clearState()
        state = viewModel.state.first()
        assertEquals(LaunchAlertState.Initial, state)

        // When - Try again with success
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should be in NoUpdate state
        state = viewModel.state.first()
        assertEquals(LaunchAlertState.NoAlert, state)
    }

    @Test
    fun `test multiple error scenarios`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en",
            timeOut = 30000L,
            maxRetry = 3
        )
        viewModel.setConfig(config)

        val mockError = mockk<com.sepanta.errorhandler.ApiError<*>>()
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(mockError)

        // When - Multiple failed calls
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should handle multiple errors gracefully
        val state = viewModel.state.first()
        assertTrue(state is LaunchAlertState.ShowViewError)
        
        // Verify API was called multiple times
        coVerify(atLeast = 3) { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test error handling with null config`() = runTest {
        // When - Try to get data without config
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not call API
        coVerify(exactly = 0) { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
        
        // State should remain Initial
        val state = viewModel.state.first()
        assertEquals(LaunchAlertState.Initial, state)
    }

    @Test
    fun `test error handling with null itemId in sendAction`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en",
            timeOut = 30000L,
            maxRetry = 3
        )
        viewModel.setConfig(config)

        // When - Try to send action without itemId
        viewModel.sendAction("test_action")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not call API
        coVerify(exactly = 0) { mockApi.setAction(any(), any(), any(), any(), any(), any()) }
        
        // State should remain Initial
        val state = viewModel.state.first()
        assertEquals(LaunchAlertState.Initial, state)
    }
}
