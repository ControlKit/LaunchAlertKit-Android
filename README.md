# 🚀 LaunchAlertKit

[![Version](https://img.shields.io/badge/version-1.0.0-blue.svg)](https://github.com/your-username/LaunchAlertKit)
[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![API](https://img.shields.io/badge/API-21%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=21)

**LaunchAlertKit** is a powerful and flexible library for displaying launch alerts in Android applications. It provides a modern, customizable UI for showing important messages, updates, and notifications when your app starts.

## ✨ Features

- 🎨 **Modern & Customizable Design** - Beautiful UI with full customization options
- 🌍 **Multi-language Support** - Display content in different languages
- 🔄 **Smart UUID Management** - Prevent duplicate alert displays
- 📱 **Jetpack Compose Compatible** - Easy integration with modern projects
- 🛡️ **Error Handling** - Complete network and API error management
- ⚡ **High Performance** - Optimized for better performance
- 🔧 **Configurable** - Full configuration through code

## 📦 Installation

### Gradle

Add to your `build.gradle` (Module: app):

```gradle
dependencies {
    implementation 'com.github.your-username:LaunchAlertKit:1.0.0'
}
```

## 🚀 Quick Start

### 1. Basic Configuration

In your `Application` class:

```kotlin
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        LaunchAlertKit.configure(
            context = this,
            config = LaunchAlertServiceConfig(
                version = BuildConfig.VERSION_NAME,
                appId = "your_app_id",
                deviceId = getDeviceId(),
                lang = Locale.getDefault().language
            )
        )
    }
}
```

### 2. Usage in Activity/Fragment

```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            MyAppTheme {
                // Main app content
                MainContent()
                
                // Display LaunchAlert
                LaunchAlertView()
            }
        }
    }
}
```

## ⚙️ Advanced Configuration

### LaunchAlertServiceConfig

```kotlin
val config = LaunchAlertServiceConfig(
    version = "1.0.0",                    // App version
    appId = "com.example.myapp",          // App identifier
    deviceId = "unique_device_id",        // Unique device identifier
    lang = "en",                          // Language (en, fa, ar, ...)
    timeOut = 30000L,                     // Request timeout (milliseconds)
    maxRetry = 3                          // Maximum retry attempts
)
```

### LaunchAlertViewConfig

```kotlin
val viewConfig = LaunchAlertViewConfig(
    title = "Important Alert",            // Default title
    message = "Default message",          // Default message
    buttonText = "Confirm",               // Confirm button text
    cancelButtonText = "Cancel",          // Cancel button text
    showCancelButton = true,              // Show cancel button
    isDismissible = true,                 // Dismissible by outside click
    backgroundColor = Color.White,         // Background color
    titleColor = Color.Black,             // Title color
    messageColor = Color.Gray,            // Message color
    buttonColor = Color.Blue,             // Button color
    cornerRadius = 16.dp,                 // Corner radius
    elevation = 8.dp                      // Elevation
)
```

## 🎨 UI Customization

LaunchAlertKit provides 5 different built-in styles to choose from:

### Available Styles

#### 1. FullScreen1 - Full Screen Alert
![FullScreen1 Style](docs/images/fullscreen1.png)
- Full screen overlay with dark background
- Large image display (216dp height)
- Centered content layout
- Yellow submit button with transparent cancel button

```kotlin
LaunchAlertView(
    config = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.FullScreen1
    )
)
```

#### 2. Popover1 - Overlapping Button Style
![Popover1 Style](docs/images/popover1.png)
- Green background with rounded corners
- Image with title and description
- Overlapping submit button (offset by -28dp)
- Close button in top-right corner

```kotlin
LaunchAlertView(
    config = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.Popover1
    )
)
```

#### 3. Popover2 - Standard Popup Style
![Popover2 Style](docs/images/popover2.png)
- Dark background with rounded corners
- Standard button layout (submit and cancel)
- Full-width buttons with proper spacing
- Orange color scheme

```kotlin
LaunchAlertView(
    config = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.Popover2
    )
)
```

#### 4. Popover3 - Alternative Popup Style
![Popover3 Style](docs/images/popover3.png)
- Alternative layout with different spacing
- Customizable colors and dimensions
- Flexible button positioning

```kotlin
LaunchAlertView(
    config = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.Popover3
    )
)
```

#### 5. Popover4 - Modern Popup Style
![Popover4 Style](docs/images/popover4.png)
- Modern design with enhanced visuals
- Optimized for mobile devices
- Clean and minimal interface

```kotlin
LaunchAlertView(
    config = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.Popover4
    )
)
```

### Custom Style Configuration

```kotlin
val customConfig = LaunchAlertViewConfig(
    // Style Selection
    launchAlertViewStyle = LaunchAlertViewStyle.Popover1,
    
    // Colors
    popupViewBackGroundColor = Color(0xFF1E1E1E),
    headerTitleColor = Color(0xFFFFFFFF),
    descriptionTitleColor = Color(0xFFCCCCCC),
    submitButtonColor = Color(0xFF007AFF),
    cancelButtonColor = Color(0xFF8E8E93),
    
    // Layout
    popupViewCornerRadius = 20.dp,
    
    // Images
    imageDrawble = R.drawable.custom_icon,
    closeImageDrawble = R.drawable.custom_close,
    placeholderImageDrawble = R.drawable.placeholder,
    errorImageDrawble = R.drawable.error_image,
    
    // Content
    headerTitle = "Custom Title",
    descriptionTitle = "Custom description text",
    submitButtonTitle = "Confirm",
    cancelButtonTitle = "Cancel"
)
```

### Built-in Images

LaunchAlertKit includes several built-in images for different purposes:

- **background.png** - Default background for FullScreen1 style
- **background2.png** - Alternative background for Popover2 style  
- **background3.png** - Additional background option
- **close.png** - Close button icon
- **seting.png** - Settings/error placeholder icon

### Custom Images

```kotlin
val viewConfig = LaunchAlertViewConfig(
    // Custom drawable resources
    imageDrawble = R.drawable.custom_icon,
    closeImageDrawble = R.drawable.custom_close,
    placeholderImageDrawble = R.drawable.placeholder,
    errorImageDrawble = R.drawable.error_image,
    
    // Custom image scaling
    contentScaleImageDrawble = ContentScale.Crop,
    
    // Custom image colors
    updateImageColor = Color.Blue,
    closeImageColor = Color.Red
)
```

## 📱 Usage Examples

### 1. Simple Alert

```kotlin
@Composable
fun SimpleAlert() {
    LaunchAlertView(
        config = LaunchAlertViewConfig(
            launchAlertViewStyle = LaunchAlertViewStyle.Popover1,
            headerTitle = "Welcome!",
            descriptionTitle = "Welcome to our app. Please read the terms and conditions.",
            submitButtonTitle = "Continue"
        )
    )
}
```

### 2. Alert with Cancel Button

```kotlin
@Composable
fun AlertWithCancel() {
    LaunchAlertView(
        config = LaunchAlertViewConfig(
            launchAlertViewStyle = LaunchAlertViewStyle.Popover2,
            headerTitle = "Update Available",
            descriptionTitle = "A new version of the app is available. Would you like to update?",
            submitButtonTitle = "Update",
            cancelButtonTitle = "Later"
        )
    )
}
```

### 3. Full Screen Alert

```kotlin
@Composable
fun FullScreenAlert() {
    LaunchAlertView(
        config = LaunchAlertViewConfig(
            launchAlertViewStyle = LaunchAlertViewStyle.FullScreen1,
            headerTitle = "Important Notice",
            descriptionTitle = "This is a full screen alert with maximum visual impact.",
            submitButtonTitle = "Continue",
            cancelButtonTitle = "Skip"
        )
    )
}
```

### 4. Custom Styled Alert

```kotlin
@Composable
fun CustomStyledAlert() {
    LaunchAlertView(
        config = LaunchAlertViewConfig(
            launchAlertViewStyle = LaunchAlertViewStyle.Popover3,
            headerTitle = "Custom Alert",
            descriptionTitle = "This alert uses custom styling and colors.",
            submitButtonTitle = "Confirm",
            cancelButtonTitle = "Cancel",
            popupViewBackGroundColor = Color(0xFF2E7D32),
            headerTitleColor = Color.White,
            submitButtonColor = Color(0xFF4CAF50)
        )
    )
}
```

## 🔧 Advanced Management

### Using ViewModel

```kotlin
class MainViewModel : ViewModel() {
    private val launchAlertViewModel: LaunchAlertViewModel = 
        LaunchAlertViewModelFactory.create()
    
    val alertState by launchAlertViewModel.state.collectAsState()
    val isDialogOpen by launchAlertViewModel.openDialog.collectAsState()
    
    fun checkForAlerts() {
        launchAlertViewModel.getData()
    }
    
    fun submitAlert() {
        launchAlertViewModel.submitDialog()
    }
    
    fun dismissAlert() {
        launchAlertViewModel.dismissDialog()
    }
}

@Composable
fun MainScreen(viewModel: MainViewModel = hiltViewModel()) {
    val alertState by viewModel.alertState
    val isDialogOpen by viewModel.isDialogOpen
    
    LaunchedEffect(Unit) {
        viewModel.checkForAlerts()
    }
    
    when (alertState) {
        is LaunchAlertState.ShowView -> {
            if (isDialogOpen) {
                LaunchAlertView(
                    config = LaunchAlertViewConfig(
                        title = alertState.data?.title ?: "Alert",
                        message = alertState.data?.description ?: "Message",
                        buttonText = alertState.data?.buttonTitle ?: "Confirm",
                        cancelButtonText = alertState.data?.cancelButtonTitle ?: "Cancel",
                        linkUrl = alertState.data?.linkUrl,
                        iconUrl = alertState.data?.iconUrl
                    ),
                    onConfirm = { viewModel.submitAlert() },
                    onCancel = { viewModel.dismissAlert() }
                )
            }
        }
        is LaunchAlertState.NoAlert -> {
            // No alert to display
        }
        is LaunchAlertState.ShowViewError -> {
            // Handle error
            ErrorDialog(error = alertState.data)
        }
    }
}
```

## 🌍 Multi-language Support

### Setting Language

```kotlin
// In Application
LaunchAlertKit.configure(
    context = this,
    config = LaunchAlertServiceConfig(
        // ... other settings
        lang = when (Locale.getDefault().language) {
            "fa" -> "fa"
            "en" -> "en"
            "ar" -> "ar"
            else -> "en"
        }
    )
)
```

### Multilingual Content

```kotlin
val multilingualConfig = LaunchAlertViewConfig(
    title = mapOf(
        "fa" to "هشدار مهم",
        "en" to "Important Alert",
        "ar" to "تنبيه مهم"
    ),
    message = mapOf(
        "fa" to "این یک پیام مهم است",
        "en" to "This is an important message",
        "ar" to "هذه رسالة مهمة"
    )
)
```

## 🔒 Security

### Security Settings

```kotlin
val secureConfig = LaunchAlertServiceConfig(
    // ... other settings
    timeOut = 15000L,        // Shorter timeout
    maxRetry = 1,            // Fewer retries
    validateSSL = true,      // SSL validation
    allowInsecureConnections = false  // No insecure connections
)
```

## 📊 Monitoring and Logging

### Enable Logging

```kotlin
// In Application
if (BuildConfig.DEBUG) {
    LaunchAlertKit.enableLogging(true)
}
```

### Trackable Events

```kotlin
LaunchAlertKit.setEventListener { event ->
    when (event) {
        is AlertShown -> {
            // Alert was shown
            analytics.track("alert_shown", mapOf(
                "alert_id" to event.alertId,
                "alert_type" to event.alertType
            ))
        }
        is AlertDismissed -> {
            // Alert was dismissed
            analytics.track("alert_dismissed", mapOf(
                "alert_id" to event.alertId
            ))
        }
        is AlertConfirmed -> {
            // Alert was confirmed
            analytics.track("alert_confirmed", mapOf(
                "alert_id" to event.alertId
            ))
        }
    }
}
```

## 🧪 Testing

### Unit Test

```kotlin
@Test
fun `test alert display logic`() = runTest {
    val viewModel = LaunchAlertViewModel(mockApi, mockLocalDataSource)
    val config = LaunchAlertServiceConfig(
        version = "1.0.0",
        appId = "test_app",
        deviceId = "test_device",
        lang = "en"
    )
    
    viewModel.setConfig(config)
    viewModel.getData()
    
    val state = viewModel.state.value
    assertTrue(state is LaunchAlertState.Action)
}
```

### UI Test

```kotlin
@Test
fun testAlertDisplay() {
    composeTestRule.setContent {
        LaunchAlertView(
            config = LaunchAlertViewConfig(
                title = "Test Alert",
                message = "Test Message"
            )
        )
    }
    
    composeTestRule.onNodeWithText("Test Alert").assertIsDisplayed()
    composeTestRule.onNodeWithText("Test Message").assertIsDisplayed()
}
```

## 🚀 Performance Optimization

### Optimal Settings

```kotlin
val optimizedConfig = LaunchAlertServiceConfig(
    // ... other settings
    timeOut = 10000L,        // Optimal timeout
    maxRetry = 2,            // Optimal retries
    cacheEnabled = true,     // Enable cache
    cacheExpiry = 3600000L   // Cache expiry (1 hour)
)
```

### Memory Management

```kotlin
class MainActivity : ComponentActivity() {
    private var launchAlertView: LaunchAlertView? = null
    
    override fun onDestroy() {
        super.onDestroy()
        // Clean up resources
        launchAlertView = null
    }
}
```

## 📋 Implementation Checklist

- [ ] Add dependency to `build.gradle`
- [ ] Configure in `Application` class
- [ ] Set up `LaunchAlertServiceConfig`
- [ ] Add `LaunchAlertView` to UI
- [ ] Configure `LaunchAlertViewConfig` (optional)
- [ ] Test functionality on different devices
- [ ] Set up required languages
- [ ] Configure security settings
- [ ] Add monitoring

## 🤝 Contributing

We welcome contributions! Please:

1. Fork the project
2. Create a new branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 📞 Support

- 📧 Email: support@example.com
- 🐛 Bug Reports: [GitHub Issues](https://github.com/your-username/LaunchAlertKit/issues)
- 💬 Discussions: [GitHub Discussions](https://github.com/your-username/LaunchAlertKit/discussions)

## 🔄 Version History

### Version 1.0.0
- ✨ Initial release
- 🎨 Modern UI with Jetpack Compose
- 🌍 Multi-language support
- 🔄 Smart UUID management
- 🛡️ Complete error handling

---

**Built with ❤️ for the Android community**