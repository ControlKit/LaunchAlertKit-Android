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
import java.util.UUID

class LaunchAlertViewModelUuidTest {

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
    fun `test getData with newer UUID shows alert`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Set up older UUID in local storage
        val olderUuid = "00000000-0000-0000-0000-000000000001"
        val newerUuid = "00000000-0000-0000-0000-000000000002"
        
        coEvery { mockLocalDataSource.getLastId() } returns olderUuid
        coEvery { mockLocalDataSource.saveLastId(any()) } returns Unit

        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = newerUuid,
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

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should show alert because newerUuid > olderUuid
        val state = viewModel.state.value
        println("DEBUG: Current state = $state")
        // After getData, state should be Action because sendAction is called automatically
        assertTrue("Expected Action but got $state", state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)

        // Verify API calls
        coVerify { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
        coVerify { mockApi.setAction(any(), any(), any(), any(), any(), "VIEW") }
        coVerify { mockLocalDataSource.saveLastId(newerUuid) }
    }

    @Test
    fun `test getData with older UUID does not show alert`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Set up newer UUID in local storage
        val newerUuid = "00000000-0000-0000-0000-000000000002"
        val olderUuid = "00000000-0000-0000-0000-000000000001"
        
        coEvery { mockLocalDataSource.getLastId() } returns newerUuid

        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = olderUuid,
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

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not show alert because olderUuid < newerUuid
        val state = viewModel.state.value
        assertEquals(LaunchAlertState.NoAlert, state)

        // Verify API was called but no action was sent
        coVerify { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockApi.setAction(any(), any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockLocalDataSource.saveLastId(any()) }
    }

    @Test
    fun `test getData with same UUID does not show alert`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Set up same UUID in local storage
        val sameUuid = "00000000-0000-0000-0000-000000000001"
        
        coEvery { mockLocalDataSource.getLastId() } returns sameUuid

        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = sameUuid,
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

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should not show alert because sameUuid == sameUuid
        val state = viewModel.state.value
        assertEquals(LaunchAlertState.NoAlert, state)

        // Verify API was called but no action was sent
        coVerify { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockApi.setAction(any(), any(), any(), any(), any(), any()) }
        coVerify(exactly = 0) { mockLocalDataSource.saveLastId(any()) }
    }

    @Test
    fun `test getData with null lastId shows alert`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Set up null lastId (first time user)
        coEvery { mockLocalDataSource.getLastId() } returns null
        coEvery { mockLocalDataSource.saveLastId(any()) } returns Unit

        val testUuid = "00000000-0000-0000-0000-000000000001"
        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = testUuid,
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

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should show alert because no previous UUID (first time user)
        val state = viewModel.state.value
        // After getData, state should be Action because sendAction is called automatically
        assertTrue(state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)

        // Verify API calls
        coVerify { mockApi.getLaunchAlertData(any(), any(), any(), any(), any()) }
        coVerify { mockApi.setAction(any(), any(), any(), any(), any(), "VIEW") }
        coVerify { mockLocalDataSource.saveLastId(testUuid) }
    }

    @Test
    fun `test UUID comparison with different formats`() = runTest {
        // Given
        val config = LaunchAlertServiceConfig(
            version = "1.0.0",
            appId = "test_app",
            deviceId = "test_device",
            lang = "en"
        )
        viewModel.setConfig(config)

        // Test with different UUID formats
        val uuid1 = "00000000-0000-0000-0000-000000000001"
        val uuid2 = "00000000-0000-0000-0000-000000000010"
        
        coEvery { mockLocalDataSource.getLastId() } returns uuid1
        coEvery { mockLocalDataSource.saveLastId(any()) } returns Unit

        val localizedText = LocalizedText(language = "en", content = "Test Title")
        val apiData = ApiData(
            id = uuid2,
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

        // When
        viewModel.getData()
        testDispatcher.scheduler.advanceUntilIdle()

        // Then - Should show alert because uuid2 > uuid1
        val state = viewModel.state.value
        // After getData, state should be Action because sendAction is called automatically
        assertTrue(state is LaunchAlertState.Action)
        val actionState = state as LaunchAlertState.Action
        assertEquals("VIEW", actionState.data)

        // Verify UUID comparison worked correctly
        assertTrue(UUID.fromString(uuid2) > UUID.fromString(uuid1))
    }
}
