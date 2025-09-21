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

class LaunchAlertViewModelAdvancedTest {

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
    fun `test getData with successful API response sets Action state`() = runTest {
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
        // After getData, state should be Action because sendAction is called automatically
        assertTrue(state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)
        
        // Verify API was called with correct parameters
        coVerify { 
            mockApi.getLaunchAlertData(
                any(), // url
                "test_app", // appId
                "1.0.0", // version
                "test_device", // deviceId
                "00000000-0000-0000-0000-000000000001" // lastId
            ) 
        }
        
        // Verify lastId was saved
        coVerify { mockLocalDataSource.saveLastId("00000000-0000-0000-0000-000000000002") }
        
        // Verify that VIEW action was sent automatically
        coVerify { 
            mockApi.setAction(
                any(), // url
                "test_app", // appId
                "1.0.0", // version
                "test_device", // sdkVersion
                any(), // deviceId
                "VIEW" // action
            ) 
        }
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
        assertEquals(LaunchAlertState.NoAlert, state)
    }

    @Test
    fun `test getData with error response sets ShowViewError state`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        val mockError = mockk<com.sepanta.errorhandler.ApiError<*>>()
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(mockError)
        coEvery { mockLocalDataSource.getLastId() } returns "00000000-0000-0000-0000-000000000001"

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.ShowViewError)
        assertEquals(mockError, (state as LaunchAlertState.ShowViewError).data)
    }

    @Test
    fun `test sendAction with valid itemId calls API`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // First set itemId by calling getData
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

        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // When - Send custom action
        viewModel.sendAction("test_action")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        assertEquals("test_action", (state as LaunchAlertState.Action).data)

        // Verify setAction was called
        coVerify { 
            mockApi.setAction(
                any(), // url
                "test_app", // appId
                "1.0.0", // version
                "test_device", // sdkVersion
                any(), // deviceId
                "test_action" // action
            ) 
        }
    }

    @Test
    fun `test sendAction without itemId does not call API`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // When - Send action without setting itemId
        viewModel.sendAction("test_action")
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not call API
        coVerify(exactly = 0) { mockApi.setAction(any(), any(), any(), any(), any(), any()) }
    }

    @Test
    fun `test submitDialog workflow`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Set itemId by calling getData first
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

        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // When - Submit dialog
        viewModel.submitDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        assertEquals("ACCEPTED", (state as LaunchAlertState.Action).data)

        // Verify ACCEPTED action was sent
        coVerify { 
            mockApi.setAction(
                any(), // url
                "test_app", // appId
                "1.0.0", // version
                "test_device", // sdkVersion
                any(), // deviceId
                "ACCEPTED" // action
            ) 
        }
    }

    @Test
    fun `test dismissDialog workflow`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Set itemId by calling getData first
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

        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // When - Dismiss dialog
        viewModel.dismissDialog()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        assertEquals("CANCELED", (state as LaunchAlertState.Action).data)

        // Verify CANCELED action was sent
        coVerify { 
            mockApi.setAction(
                any(), // url
                "test_app", // appId
                "1.0.0", // version
                "test_device", // sdkVersion
                any(), // deviceId
                "CANCELED" // action
            ) 
        }
    }

    @Test
    fun `test state transitions from Initial to Action`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Initial state
        assertEquals(LaunchAlertState.Initial, viewModel.state.value)

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

        // Then - Should transition to Action state
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        assertEquals("VIEW", (state as LaunchAlertState.Action).data)
    }

    @Test
    fun `test getData automatically sends VIEW action when response is valid`() = runTest {
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

        // Then - Should automatically send VIEW action and be in Action state
        val state = viewModel.state.value
        assertTrue(state is LaunchAlertState.Action)
        assertEquals("VIEW", (state as LaunchAlertState.Action).data)

        // Verify both API calls were made
        coVerify { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
        coVerify { mockApi.setAction(any(), any(), any(), any(), any(), "VIEW") }
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
        val response = ApiCheckUpdateResponse(apiData)

        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(response)
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

        // Second call - null response
        coEvery { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(null)

        // When - Second call
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Second response
        state = viewModel.state.value
        assertEquals(LaunchAlertState.NoAlert, state)
    }
}
