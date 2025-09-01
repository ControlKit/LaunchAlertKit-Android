package com.sepanta.controlkit.launchalertkit.view.viewmodel

import app.cash.turbine.test
import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.service.LaunchAlertApi
import com.sepanta.controlkit.launchalertkit.service.apiError.ApiError
import com.sepanta.controlkit.launchalertkit.service.apiError.NetworkResult
import com.sepanta.controlkit.launchalertkit.service.local.LocalDataSource
import com.sepanta.controlkit.launchalertkit.service.model.ApiCheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.service.model.ApiData
import com.sepanta.controlkit.launchalertkit.service.model.toDomain
import com.sepanta.controlkit.launchalertkit.view.viewmodel.state.LaunchAlertState
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LaunchAlertViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private lateinit var api: LaunchAlertApi
    private lateinit var localDataSource: LocalDataSource
    private lateinit var viewModel: LaunchAlertViewModel
    private val config = LaunchAlertServiceConfig(
        route = "testRoute",
        appId = "testAppId",
        version = "1.0",
        deviceId = "device123"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        api = mockk()
        localDataSource = mockk()
        viewModel = LaunchAlertViewModel(api, localDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    // --- STATE TESTS ---

    @Test
    fun `initial state should be Initial`() {
        assertEquals(LaunchAlertState.Initial, viewModel.state.value)
    }

    @Test
    fun `setConfig with valid response should set Update and save lastId`() = runTest {
        val apiResponse = ApiCheckUpdateResponse(
            data = ApiData(
                id = "123",
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
        val domainResponse = apiResponse.toDomain()

        coEvery { api.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(apiResponse)
        coEvery { localDataSource.getLastId() } returns null
        coEvery { localDataSource.saveLastId("123") } just Runs

        viewModel.setConfig(config)
        advanceUntilIdle()

        assertEquals(LaunchAlertState.Update(domainResponse), viewModel.state.value)
        coVerify { localDataSource.saveLastId("123") }
    }

    @Test
    fun `setConfig with null response should set NoUpdate`() = runTest {
        val apiResponse = ApiCheckUpdateResponse(
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

        coEvery { api.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Success(apiResponse)
        coEvery { localDataSource.getLastId() } returns null

        viewModel.setConfig(config)
        advanceUntilIdle()

        assertEquals(LaunchAlertState.NoUpdate, viewModel.state.value)
    }

    @Test
    fun `setConfig with error should set Error state`() = runTest {
        val error = ApiError("Something went wrong", ApiError.ErrorStatus.UNKNOWN_ERROR)

        coEvery { api.getLaunchAlertData(any(), any(), any(), any(), any()) } returns NetworkResult.Error(error)
        coEvery { localDataSource.getLastId() } returns null

        viewModel.setConfig(config)
        advanceUntilIdle()

        assertEquals(LaunchAlertState.Error(error), viewModel.state.value)
    }


    @Test
    fun `clearState should reset to Initial`() = runTest {
        viewModel.clearState()
        assertEquals(LaunchAlertState.Initial, viewModel.state.value)
    }

    // --- DIALOG STATE TESTS ---

    @Test
    fun `showDialog should set openDialog true`() {
        viewModel.showDialog()
        assertEquals(true, viewModel.openDialog.value)
    }

    @Test
    fun `submitDialog should close dialog and reset state`() {
        viewModel.submitDialog()
        assertEquals(false, viewModel.openDialog.value)
        assertEquals(LaunchAlertState.Initial, viewModel.state.value)
    }

    @Test
    fun `dismissDialog should close dialog, reset state and trigger launchAlert event`() = runTest {
        viewModel.launchAlertEvent.test {
            viewModel.dismissDialog()
            assertEquals(false, viewModel.openDialog.value)
            assertEquals(LaunchAlertState.Initial, viewModel.state.value)
            assertEquals(Unit, awaitItem())
            cancelAndIgnoreRemainingEvents()
        }
    }
}
