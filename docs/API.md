# API Documentation

## ForceUpdateKit

### forceUpdateKitHost

The main Composable function to initialize and manage the ForceUpdateKit.

```kotlin
@Composable
fun forceUpdateKitHost(
    config: ForceUpdateServiceConfig,
    onDismiss: (() -> Unit)? = null,
    onState: ((ForceUpdateState) -> Unit)? = null
): ForceUpdateKit
```

**Parameters:**
- `config`: Service configuration object
- `onDismiss`: Callback when dialog is dismissed
- `onState`: Callback for state changes

**Returns:** ForceUpdateKit instance

## ForceUpdateServiceConfig

Configuration class for the service layer.

```kotlin
data class ForceUpdateServiceConfig(
    var viewConfig: ForceUpdateViewConfig = ForceUpdateViewConfig(),
    var version: String,
    var appId: String,
    var deviceId: String = "1",
    var skipException: Boolean = false,
    var timeOut: Long = 5000L,
    var timeRetryThreadSleep: Long = 1000L,
    var maxRetry: Int = 5,
    var canDismissRetryView: Boolean = false,
    var lang: String = "en"
)
```

**Properties:**
- `viewConfig`: View configuration object
- `version`: Current app version (required)
- `appId`: Unique app identifier (required)
- `deviceId`: Device identifier
- `skipException`: Skip errors silently
- `timeOut`: API timeout in milliseconds
- `timeRetryThreadSleep`: Retry delay
- `maxRetry`: Maximum retry attempts
- `canDismissRetryView`: Allow dismissing retry view
- `lang`: Language code

## ForceUpdateViewConfig

Configuration class for UI customization.

```kotlin
data class ForceUpdateViewConfig(
    var forceUpdateViewStyle: ForceUpdateViewStyle = ForceUpdateViewStyle.Popover1,
    var placeholderImageDrawble: Int? = null,
    var imageDrawble: Int? = null,
    var errorImageDrawble: Int? = null,
    var contentScaleImageDrawble: ContentScale? = null,
    var updateImageColor: Color? = null,
    var imageLayoutModifier: Modifier? = null,
    var popupViewLayoutModifier: Modifier? = null,
    var popupViewBackGroundColor: Color? = null,
    var popupViewCornerRadius: Dp? = null,
    var headerTitle: String = "It's time to update",
    var headerTitleColor: Color? = null,
    var headerTitleLayoutModifier: Modifier? = null,
    var descriptionTitle: String = "The version you are using is old...",
    var descriptionTitleColor: Color? = null,
    var descriptionTitleLayoutModifier: Modifier? = null,
    var lineTitleColor: Color? = null,
    var lineLayoutModifier: Modifier? = null,
    var buttonTitle: String = "Update New Version",
    var buttonTitleColor: Color? = null,
    var buttonColor: Color? = null,
    var buttonCornerRadius: Dp? = null,
    var buttonBorderColor: Color? = null,
    var buttonLayoutModifier: Modifier? = null,
    var versionTitle: String = "Up to 12.349 version Apr 2024.",
    var versionTitleColor: Color? = null,
    var versionTitleLayoutModifier: Modifier? = null,
    
    // Custom Views
    var imageView: @Composable ((String) -> Unit)? = null,
    var versionTitleView: @Composable ((String) -> Unit)? = null,
    var headerTitleView: @Composable ((String) -> Unit)? = null,
    var descriptionTitleView: @Composable ((String) -> Unit)? = null,
    var lineView: @Composable (() -> Unit)? = null,
    var buttonView: @Composable ((() -> Unit) -> Unit)? = null,
    var noUpdateState: (() -> Unit)? = null,
    
    // Retry View Configuration
    var retryViewTitleLayoutModifier: Modifier? = null,
    var retryViewTitleView: @Composable (() -> Unit)? = null,
    var retryViewTitle: String = "Connection Lost",
    var retryViewTitleColor: Color? = null,
    var retryViewTryAgainButtonLayoutModifier: Modifier? = null,
    var retryViewTryAgainButtonView: @Composable (() -> Unit)? = null,
    var retryViewTryAgainButtonCornerRadius: Dp? = null,
    var retryViewTryAgainButtonColor: Color? = null,
    var retryViewTryAgainButtonTitle: String = "Try Again",
    var retryViewCancelButtonLayoutModifier: Modifier? = null,
    var retryViewCancelButtonView: @Composable (() -> Unit)? = null,
    var retryViewCancelButtonCornerRadius: Dp? = null,
    var retryViewCancelButtonColor: Color? = null,
    var retryViewCancelButtonTitle: String = "Cancel",
    var retryViewImageLayoutModifier: Modifier? = null,
    var retryViewImageView: @Composable (() -> Unit)? = null,
    var retryViewUpdateImageDrawble: Int? = null,
    var retryViewImageColor: Color = Black100,
    var retryViewSpace: @Composable (() -> Unit)? = null,
    var retryViewCancelButton: (() -> Unit)? = null
)
```

## ForceUpdateViewStyle

Enum for available UI styles.

```kotlin
enum class ForceUpdateViewStyle {
    FullScreen1,
    FullScreen2,
    FullScreen3,
    FullScreen4,
    Popover1,
    Popover2
}
```

## ForceUpdateState

Sealed class representing different states of the update process.

```kotlin
sealed class ForceUpdateState {
    object Initial : ForceUpdateState()
    object NoUpdate : ForceUpdateState()
    data class UpdateError(val data: ApiError?) : ForceUpdateState()
    object Update : ForceUpdateState()
    data class ShowView(val data: CheckUpdateResponse?) : ForceUpdateState()
    data class ShowViewError(val data: ApiError?) : ForceUpdateState()
    object SkipError : ForceUpdateState()
}
```

## CheckUpdateResponse

Data class representing the update response from the server.

```kotlin
data class CheckUpdateResponse(
    val id: String? = null,
    val version: String? = null,
    val title: String? = null,
    val forceUpdate: Boolean? = null,
    val description: String? = null,
    val iconUrl: String? = null,
    val linkUrl: String? = null,
    val buttonTitle: String? = null,
    val cancelButtonTitle: String? = null,
    val sdkVersion: String? = null,
    val minimumVersion: String? = null,
    val maximumVersion: String? = null,
    val createdAt: String? = null
)
```

## ApiError

Data class representing API errors. Uses the external error handling library.

```kotlin
// From com.sepanta.errorhandler.ApiError
data class ApiError<T>(
    val message: String? = null,
    val code: Int? = null,
    val details: String? = null,
    val data: T? = null
)
```

## ForceUpdateKit

Main class for managing the update process.

### Methods

#### showView()
Triggers the update check and shows the appropriate UI.

```kotlin
fun showView()
```

#### viewModel
Access to the underlying ViewModel for advanced usage.

```kotlin
val viewModel: ForceUpdateViewModel
```

## ForceUpdateViewModel

ViewModel class for managing the update state and API calls.

### Methods

#### getData()
Fetches update information from the server.

```kotlin
fun getData()
```

#### sendAction(action: String)
Sends an action (VIEW or UPDATE) to the server.

```kotlin
fun sendAction(action: String)
```

#### tryAgain()
Retries the last failed operation.

```kotlin
fun tryAgain()
```

#### clearState()
Clears the current state.

```kotlin
fun clearState()
```

#### showDialog()
Shows the update dialog.

```kotlin
fun showDialog()
```

#### dismissDialog()
Dismisses the update dialog.

```kotlin
fun dismissDialog()
```

#### submit()
Submits the update action.

```kotlin
fun submit()
```

### Properties

#### state
StateFlow containing the current state.

```kotlin
val state: StateFlow<ForceUpdateState>
```

#### openDialog
StateFlow indicating if the dialog is open.

```kotlin
val openDialog: StateFlow<Boolean>
```

#### forceUpdateEvent
Flow for force update events.

```kotlin
val forceUpdateEvent: Flow<Unit>
```

## Actions

Enum for tracking user actions.

```kotlin
enum class Actions(val value: String) {
    VIEW("VIEW"),
    UPDATE("UPDATE")
}
```
