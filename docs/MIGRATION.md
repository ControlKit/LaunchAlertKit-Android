# Migration Guide

This guide helps you migrate between different versions of LaunchAlertKit.

## Migrating from 0.0.1 to 0.0.2

### Breaking Changes

#### 1. Configuration Structure
The configuration structure has been updated for better organization.

**Before (0.0.1):**
```kotlin
LaunchAlertServiceConfig(
    version = "1.0.0",
    appId = "your-app-id",
    deviceId = "device-id"
    // Other properties mixed together
)
```

**After (0.0.2):**
```kotlin
LaunchAlertServiceConfig(
    version = "1.0.0",
    appId = "your-app-id",
    deviceId = "device-id",
    viewConfig = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.FullScreen1
        // UI-specific configurations
    )
)
```

#### 2. State Management
State handling has been improved with better error management and external error handling library.

**Before (0.0.1):**
```kotlin
onState = { state ->
    when (state) {
        is ShowAlert -> { /* Handle alert */ }
        is ShowError -> { /* Handle error */ }
    }
}
```

**After (0.0.2):**
```kotlin
onState = { state ->
    when (state) {
        is LaunchAlertState.ShowView -> { /* Handle alert */ }
        is LaunchAlertState.ShowViewError -> { /* Handle error */ }
        is LaunchAlertState.ActionError -> { /* Handle action error */ }
        is LaunchAlertState.NoAlert -> { /* No alert to show */ }
    }
}
```

**Note**: Error types now use `com.sepanta.errorhandler.ApiError<*>` instead of custom error types.

#### 3. API Integration Changes
API integration has been updated with new authentication method and configurable base URL.

**Before (0.0.1):**
```kotlin
// Query parameters
GET /launch-alerts?appId={appId}&version={version}&deviceId={deviceId}&sdkVersion={sdkVersion}
```

**After (0.0.2):**
```kotlin
// Header-based authentication
GET /launch-alerts
Headers:
  - x-app-id: {appId}
  - x-version: {version}
  - x-sdk-version: {sdkVersion}
  - x-device-uuid: {deviceId}
```

**Configuration:**
```properties
# local.properties
API_URL="https://your-api-domain.com/api/launch-alerts"
```

#### 4. UI Customization
UI customization has been enhanced with more options.

**Before (0.0.1):**
```kotlin
// Limited customization options
LaunchAlertServiceConfig(
    // Basic properties only
)
```

**After (0.0.2):**
```kotlin
LaunchAlertServiceConfig(
    viewConfig = LaunchAlertViewConfig(
        launchAlertViewStyle = LaunchAlertViewStyle.FullScreen1,
        imageDrawble = R.drawable.custom_image,
        submitButtonColor = Color.Blue,
        headerTitle = "Custom Title",
        // Many more customization options
    )
)
```

### New Features

#### 1. External Error Handling Library
Integration with `com.sepanta.errorhandler` for better error management:

```kotlin
// Automatic error entity registration
fun setupErrorEntities() {
    ErrorEntityRegistry.register(ErrorValidation::class.java)
}
```

#### 2. Configurable API URL
API URL is now configurable via `local.properties`:

```properties
# local.properties
API_URL="https://your-api-domain.com/api/launch-alerts"
```

**Benefits:**
- Easy environment switching (dev, staging, production)
- No hardcoded URLs in source code
- Secure configuration management

#### 3. Multiple UI Styles
Five different UI styles are now available:

```kotlin
LaunchAlertViewConfig(
    launchAlertViewStyle = LaunchAlertViewStyle.FullScreen1  // or Popover1, Popover2, Popover3, Popover4
)
```

#### 4. Multi-language Support
Support for localized content:

```kotlin
LaunchAlertServiceConfig(
    lang = "fa"  // Persian, Arabic, English, etc.
)
```

#### 5. Custom Views
Advanced customization with custom Composable views:

```kotlin
LaunchAlertViewConfig(
    imageView = { imageUrl ->
        AsyncImage(
            model = imageUrl,
            contentDescription = "Alert image"
        )
    },
    submitButtonView = { onClick ->
        Button(onClick = onClick) {
            Text("Custom Button")
        }
    }
)
```

#### 6. Enhanced Error Handling
Better error management with retry mechanisms:

```kotlin
LaunchAlertServiceConfig(
    maxRetry = 3,
    timeOut = 30000L
)
```

### Migration Steps

1. **Update Dependencies**
   ```gradle
   implementation 'com.github.ControlKit:LaunchAlertKit-Android:0.0.2'
   ```

2. **Configure API URL**
   ```properties
   # local.properties
   API_URL="https://your-api-domain.com/api/launch-alerts"
   ```

3. **Update Configuration**
   ```kotlin
   // Wrap existing config in viewConfig
   LaunchAlertServiceConfig(
       version = "1.0.0",
       appId = "your-app-id",
       deviceId = "device-id",
       viewConfig = LaunchAlertViewConfig(
           launchAlertViewStyle = LaunchAlertViewStyle.FullScreen1
       )
   )
   ```

4. **Update State Handling**
   ```kotlin
   onState = { state ->
       when (state) {
           is LaunchAlertState.ShowView -> {
               // Handle alert dialog
           }
           is LaunchAlertState.ShowViewError -> {
               // Handle network errors
           }
           is LaunchAlertState.ActionError -> {
               // Handle action errors
           }
           is LaunchAlertState.NoAlert -> {
               // No alert available
           }
       }
   }
   ```

5. **Test Your Implementation**
   - Verify all states are handled correctly
   - Test error scenarios
   - Ensure UI displays properly
   - Test with different network conditions

### Deprecated Features

The following features from 0.0.1 are deprecated and will be removed in future versions:

- Direct configuration properties (moved to `viewConfig`)
- Simple state handling (replaced with comprehensive state management)
- Basic error handling (replaced with enhanced error management)
- Query parameter-based API authentication (replaced with header-based authentication)
- Custom error types (replaced with external error handling library)

### Troubleshooting

#### Common Issues

1. **State Not Updating**
   - Ensure you're handling all state cases
   - Check if `skipException` is set correctly

2. **UI Not Displaying**
   - Verify `forceUpdateViewStyle` is set correctly
   - Check if `viewConfig` is properly configured

3. **API Errors**
   - Verify API endpoint is accessible
   - Check network permissions
   - Ensure proper error handling
   - Verify header-based authentication is working
   - Check external error handling library integration
   - Verify API_URL is correctly set in local.properties

#### Getting Help

If you encounter issues during migration:

1. Check the [API Documentation](API.md)
2. Review the [README](README.md) for examples
3. Open an issue on [GitHub](https://github.com/ControlKit/LaunchAlertKit-Android/issues)
4. Contact support at support@controlkit.com

### Future Versions

Planned features for upcoming versions:

- **0.0.3**: Enhanced caching mechanisms
- **0.0.4**: Additional UI themes
- **0.0.5**: Advanced analytics integration
- **1.0.0**: Stable API with long-term support

Stay updated by watching the repository and checking the [Changelog](CHANGELOG.md).
