package com.sepanta.controlkit.launchalertkit.view.viewmodel

import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
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
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchAlertViewModelBasicTest {

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
    fun `test initial state is Initial`() = runTest {
        // When
        val state = viewModel.state.first()

        // Then
        assertEquals(LaunchAlertState.Initial, state)
    }

    @Test
    fun `test setConfig works correctly`() {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en",
            timeOut = 30000L,
            maxRetry = 3
        )

        // When
        viewModel.setConfig(config)

        // Then - No exception thrown, config is set
        // We can verify this by calling getData which should call API now
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()
        
        coVerify { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test clearState resets state to Initial`() = runTest {
        // When
        viewModel.clearState()

        // Then
        val state = viewModel.state.first()
        assertEquals(LaunchAlertState.Initial, state)
    }

    @Test
    fun `test getData with null config returns early`() = runTest {
        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test sendAction with null itemId returns early`() = runTest {
        // When
        viewModel.sendAction("test_action")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify(exactly = 0) { mockApi.setAction(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test showDialog sets openDialog to true`() = runTest {
        // When
        viewModel.showDialog()

        // Then
        val openDialog = viewModel.openDialog.first()
        assertTrue(openDialog)
    }

    @Test
    fun `test openDialog initial state is true`() = runTest {
        // Then
        val openDialog = viewModel.openDialog.first()
        assertTrue(openDialog)
    }

    @Test
    fun `test submitDialog closes dialog and clears state`() = runTest {
        // When
        viewModel.submitDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val openDialog = viewModel.openDialog.first()
        assertFalse(openDialog)
        
        val state = viewModel.state.first()
        assertEquals(LaunchAlertState.Initial, state)
    }

    @Test
    fun `test dismissDialog closes dialog and clears state`() = runTest {
        // When
        viewModel.dismissDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val openDialog = viewModel.openDialog.first()
        assertFalse(openDialog)
        
        val state = viewModel.state.first()
        assertEquals(LaunchAlertState.Initial, state)
    }

    @Test
    fun `test triggerLaunchAlert sends event`() = runTest {
        // When
        viewModel.triggerLaunchAlert()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val event = viewModel.launchAlertEvent.first()
        assertEquals(Unit, event)
    }

    @Test
    fun `test config parameters are properly used in API calls`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "2.0.0",
            appId = "my_app",
            deviceId = "device_123",
            lang = "fa",
            timeOut = 60000L,
            maxRetry = 5
        )
        viewModel.setConfig(config)

        coEvery { mockLocalDataSource.getLastId() } returns "last_123"

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        coVerify { 
            mockApi.getLaunchAlertData(
                any(), // url
                "my_app", // appId
                "2.0.0", // version
                "device_123", // deviceId
                "last_123" // lastId
            ) 
        }
    }
}
