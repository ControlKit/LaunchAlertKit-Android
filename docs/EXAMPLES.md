# Examples

This document provides comprehensive examples of using ForceUpdateKit in various scenarios.

## Basic Examples

### Simple Integration

```kotlin
@Composable
fun MyApp() {
    val kit = forceUpdateKitHost(
        config = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "com.example.myapp",
            deviceId = "device-123",
            viewConfig = ForceUpdateViewConfig(
                forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1
            )
        )
    )
    
    kit.showView()
}
```

### With State Handling

```kotlin
@Composable
fun MyApp() {
    val kit = forceUpdateKitHost(
        config = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "com.example.myapp",
            deviceId = "device-123",
            viewConfig = ForceUpdateViewConfig(
                forceUpdateViewStyle = ForceUpdateViewStyle.Popover1
            )
        ),
        onState = { state ->
            when (state) {
                is ForceUpdateState.ShowView -> {
                    Log.d("ForceUpdate", "Showing update dialog")
                }
                is ForceUpdateState.NoUpdate -> {
                    Log.d("ForceUpdate", "No update available")
                }
                is ForceUpdateState.ShowViewError -> {
                    Log.e("ForceUpdate", "Error: ${state.data?.message}")
                }
                is ForceUpdateState.UpdateError -> {
                    Log.e("ForceUpdate", "Update error: ${state.data?.message}")
                }
                is ForceUpdateState.SkipError -> {
                    Log.i("ForceUpdate", "Error skipped")
                }
            }
        }
    )
    
    kit.showView()
}
```

## UI Style Examples

### FullScreen Styles

#### FullScreen1 - Clean Design
```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
    headerTitle = "New Version Available",
    descriptionTitle = "We've added exciting new features and improvements!",
    buttonTitle = "Update Now",
    popupViewBackGroundColor = Color.White,
    buttonColor = Color(0xFF2196F3)
)
```

#### FullScreen2 - Detailed Layout
```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen2,
    headerTitle = "Update Required",
    descriptionTitle = "This version includes important security updates and new features that enhance your experience.",
    buttonTitle = "Get Latest Version",
    versionTitle = "Version 2.1.0 - December 2024",
    lineTitleColor = Color.Gray,
    popupViewBackGroundColor = Color(0xFFF5F5F5)
)
```

#### FullScreen3 - Balanced Design
```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen3,
    headerTitle = "Time to Update",
    descriptionTitle = "Your app is running an older version. Update now to enjoy the latest features and improvements.",
    buttonTitle = "Update",
    versionTitle = "Latest: v2.1.0",
    headerTitleColor = Color(0xFF2E7D32),
    buttonColor = Color(0xFF4CAF50)
)
```

#### FullScreen4 - Modern Layout
```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen4,
    headerTitle = "App Update",
    descriptionTitle = "A new version is available with enhanced performance and new features.",
    buttonTitle = "Update App",
    popupViewBackGroundColor = Color(0xFF1E1E1E),
    headerTitleColor = Color.White,
    descriptionTitleColor = Color(0xFFB0B0B0),
    buttonColor = Color(0xFFBB86FC)
)
```

### Popover Styles

#### Popover1 - Compact Dialog
```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.Popover1,
    headerTitle = "Update Available",
    descriptionTitle = "New features and improvements are waiting for you!",
    buttonTitle = "Update",
    popupViewCornerRadius = 20.dp,
    popupViewBackGroundColor = Color.White,
    buttonColor = Color(0xFF1976D2)
)
```

#### Popover2 - Enhanced Dialog
```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.Popover2,
    headerTitle = "App Update",
    descriptionTitle = "Update to the latest version for better performance and new features.",
    buttonTitle = "Update Now",
    versionTitle = "v2.1.0 Available",
    popupViewCornerRadius = 16.dp,
    popupViewBackGroundColor = Color(0xFFF8F9FA),
    headerTitleColor = Color(0xFF1A1A1A),
    buttonColor = Color(0xFF007AFF)
)
```

## Customization Examples

### Custom Colors and Styling

```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
    
    // Colors
    popupViewBackGroundColor = Color(0xFF1A1A1A),
    headerTitleColor = Color.White,
    descriptionTitleColor = Color(0xFFCCCCCC),
    buttonColor = Color(0xFF007AFF),
    buttonCornerRadius = 12.dp,
    
    // Text Content
    headerTitle = "🚀 New Update Available",
    descriptionTitle = "We've packed this update with amazing new features and performance improvements!",
    buttonTitle = "Update Now",
    versionTitle = "Version 2.1.0 - December 2024"
)
```

### Custom Images

```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
    
    // Custom image resource
    imageDrawble = R.drawable.update_illustration,
    contentScaleImageDrawble = ContentScale.Fit,
    
    // Placeholder and error images
    placeholderImageDrawble = R.drawable.loading_placeholder,
    errorImageDrawble = R.drawable.error_icon,
    
    // Custom image color
    updateImageColor = Color(0xFF4CAF50)
)
```

### Custom Views (Advanced)

```kotlin
ForceUpdateViewConfig(
    forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
    
    // Custom image view
    imageView = { imageUrl ->
        Card(
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = "Update illustration",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                placeholder = painterResource(R.drawable.placeholder),
                error = painterResource(R.drawable.error_icon)
            )
        }
    },
    
    // Custom header
    headerTitleView = { title ->
        Text(
            text = title,
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(16.dp)
        )
    },
    
    // Custom description
    descriptionTitleView = { description ->
        Text(
            text = description,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurface,
            textAlign = TextAlign.Center,
            lineHeight = 24.sp,
            modifier = Modifier.padding(horizontal = 24.dp)
        )
    },
    
    // Custom button
    buttonView = { onClick ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = { /* Handle later */ },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Later")
            }
            
            Button(
                onClick = onClick,
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("Update")
            }
        }
    }
)
```

## Error Handling Examples

### Basic Error Handling

```kotlin
val kit = forceUpdateKitHost(
    config = ForceUpdateServiceConfig(
        version = "1.0.0",
        appId = "com.example.myapp",
        deviceId = "device-123",
        skipException = false,  // Don't skip errors
        maxRetry = 3,          // Retry 3 times
        timeRetryThreadSleep = 2000L  // Wait 2 seconds between retries
    ),
    onState = { state ->
        when (state) {
            is ForceUpdateState.ShowViewError -> {
                // Show user-friendly error message
                showSnackbar("Unable to check for updates. Please check your internet connection.")
            }
            is ForceUpdateState.UpdateError -> {
                // Handle update action error
                showSnackbar("Update failed. Please try again later.")
            }
            is ForceUpdateState.SkipError -> {
                // Error was skipped (when skipException = true)
                Log.i("ForceUpdate", "Update check skipped due to error")
            }
        }
    }
)
```

### Advanced Error Handling with Custom Retry

```kotlin
@Composable
fun MyApp() {
    var showCustomError by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }
    
    val kit = forceUpdateKitHost(
        config = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "com.example.myapp",
            deviceId = "device-123",
            skipException = false,
            maxRetry = 2,
            timeRetryThreadSleep = 3000L
        ),
        onState = { state ->
            when (state) {
                is ForceUpdateState.ShowViewError -> {
                    errorMessage = "Network error: ${state.data?.message ?: "Unknown error"}"
                    showCustomError = true
                }
                is ForceUpdateState.UpdateError -> {
                    errorMessage = "Update failed: ${state.data?.message ?: "Unknown error"}"
                    showCustomError = true
                }
            }
        }
    )
    
    if (showCustomError) {
        CustomErrorDialog(
            message = errorMessage,
            onRetry = {
                showCustomError = false
                kit.showView()
            },
            onDismiss = {
                showCustomError = false
            }
        )
    }
    
    kit.showView()
}

@Composable
fun CustomErrorDialog(
    message: String,
    onRetry: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Update Error") },
        text = { Text(message) },
        confirmButton = {
            TextButton(onClick = onRetry) {
                Text("Retry")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}
```

## Multi-language Examples

### Persian (Farsi) Support

```kotlin
ForceUpdateServiceConfig(
    version = "1.0.0",
    appId = "com.example.myapp",
    deviceId = "device-123",
    lang = "fa",  // Persian
    viewConfig = ForceUpdateViewConfig(
        forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
        headerTitle = "آپدیت جدید موجود است",
        descriptionTitle = "نسخه جدید با ویژگی‌های جالب و بهبودهای عملکرد در دسترس است!",
        buttonTitle = "آپدیت کن",
        versionTitle = "نسخه ۲.۱.۰ - دسامبر ۲۰۲۴"
    )
)
```

### Arabic Support

```kotlin
ForceUpdateServiceConfig(
    version = "1.0.0",
    appId = "com.example.myapp",
    deviceId = "device-123",
    lang = "ar",  // Arabic
    viewConfig = ForceUpdateViewConfig(
        forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
        headerTitle = "تحديث جديد متاح",
        descriptionTitle = "إصدار جديد مع ميزات رائعة وتحسينات في الأداء متاح الآن!",
        buttonTitle = "تحديث الآن",
        versionTitle = "الإصدار ٢.١.٠ - ديسمبر ٢٠٢٤"
    )
)
```

## Integration Examples

### With Material 3 Theme

```kotlin
@Composable
fun MyApp() {
    val kit = forceUpdateKitHost(
        config = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "com.example.myapp",
            deviceId = "device-123",
            viewConfig = ForceUpdateViewConfig(
                forceUpdateViewStyle = ForceUpdateViewStyle.Popover1,
                
                // Use Material 3 colors
                popupViewBackGroundColor = MaterialTheme.colorScheme.surface,
                headerTitleColor = MaterialTheme.colorScheme.onSurface,
                descriptionTitleColor = MaterialTheme.colorScheme.onSurfaceVariant,
                buttonColor = MaterialTheme.colorScheme.primary,
                buttonCornerRadius = 12.dp
            )
        )
    )
    
    kit.showView()
}
```

### With Custom Theme

```kotlin
@Composable
fun MyApp() {
    val customColors = CustomColors(
        primary = Color(0xFF6200EE),
        secondary = Color(0xFF03DAC6),
        surface = Color(0xFF121212),
        onSurface = Color.White
    )
    
    val kit = forceUpdateKitHost(
        config = ForceUpdateServiceConfig(
            version = "1.0.0",
            appId = "com.example.myapp",
            deviceId = "device-123",
            viewConfig = ForceUpdateViewConfig(
                forceUpdateViewStyle = ForceUpdateViewStyle.FullScreen1,
                popupViewBackGroundColor = customColors.surface,
                headerTitleColor = customColors.onSurface,
                descriptionTitleColor = customColors.onSurface.copy(alpha = 0.7f),
                buttonColor = customColors.primary
            )
        )
    )
    
    kit.showView()
}
```

## Testing Examples

### Unit Test Example

```kotlin
@Test
fun `should show update dialog when update available`() = runTest {
    // Given
    val mockApi = mockk<ForceUpdateApi>()
    val mockResponse = CheckUpdateResponse(
        id = "1",
        version = "2.0.0",
        title = "Update Available",
        forceUpdate = true,
        description = "New features available"
    )
    
    coEvery { mockApi.getForceUpdateData(any(), any(), any(), any(), any()) } returns 
        NetworkResult.Success(ApiCheckUpdateResponse(ApiData(...)))
    
    val viewModel = ForceUpdateViewModel(mockApi)
    viewModel.setConfig(ForceUpdateServiceConfig(...))
    
    // When
    viewModel.getData()
    
    // Then
    val state = viewModel.state.value
    assertTrue(state is ForceUpdateState.ShowView)
}
```

### UI Test Example

```kotlin
@Test
fun testUpdateDialogDisplay() {
    // Launch the app
    composeTestRule.setContent {
        MyApp()
    }
    
    // Wait for update dialog
    composeTestRule.waitForIdle()
    
    // Verify dialog is displayed
    composeTestRule.onNodeWithText("Update Available").assertIsDisplayed()
    composeTestRule.onNodeWithText("Update Now").assertIsDisplayed()
    
    // Click update button
    composeTestRule.onNodeWithText("Update Now").performClick()
    
    // Verify button click
    composeTestRule.waitForIdle()
}
```

## Performance Examples

### Lazy Loading

```kotlin
@Composable
fun MyApp() {
    val kit by remember {
        derivedStateOf {
            forceUpdateKitHost(
                config = ForceUpdateServiceConfig(...)
            )
        }
    }
    
    LaunchedEffect(Unit) {
        // Only check for updates when app starts
        kit.showView()
    }
}
```

### Conditional Loading

```kotlin
@Composable
fun MyApp() {
    var shouldCheckUpdate by remember { mutableStateOf(false) }
    
    if (shouldCheckUpdate) {
        val kit = forceUpdateKitHost(
            config = ForceUpdateServiceConfig(...)
        )
        
        LaunchedEffect(Unit) {
            kit.showView()
        }
    }
    
    // Trigger update check based on conditions
    LaunchedEffect(Unit) {
        delay(5000) // Wait 5 seconds after app start
        shouldCheckUpdate = true
    }
}
```

These examples demonstrate the flexibility and power of ForceUpdateKit. Choose the approach that best fits your app's needs and design requirements.
