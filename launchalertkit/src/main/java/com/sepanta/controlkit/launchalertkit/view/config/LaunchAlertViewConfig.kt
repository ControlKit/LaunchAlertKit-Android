package com.sepanta.controlkit.launchalertkit.view.config

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp

data class LaunchAlertViewConfig(
    var launchAlertViewStyle: LaunchAlertViewStyle = LaunchAlertViewStyle.Popover1,
    var closeImageDrawble:   Int? = null,
    var placeholderImageDrawble:   Int? = null,
    var imageDrawble:   Int? = null,
    var errorImageDrawble:   Int? = null,
    var contentScaleImageDrawble:   ContentScale? = null,
    var updateImageColor:   Color? = null,
    var closeImageColor:   Color? = null,
    var closeImageLayoutModifier:   Modifier? = null,
    var imageLayoutModifier:   Modifier? = null,
    var popupViewLayoutModifier: Modifier? = null,
    var popupViewBackGroundColor: Color? = null,
    var popupViewCornerRadius: Dp? = null,
    var headerTitle: String = "DON'T MISS OFF",
    var headerTitleColor: Color? = null,
    var headerTitleLayoutModifier:   Modifier? = null,
    var descriptionTitle: String = "FREE VOUCHER free shipping \n" +
            "you can shop as much as you want until tomorrow !",
    var noUpdateState: (() -> Unit)? = null,

    var descriptionTitleColor: Color? = null,
    var descriptionTitleLayoutModifier:   Modifier? = null,
    var lineTitleColor: Color? = null,
    var lineLayoutModifier:   Modifier? = null,
    var submitButtonTitle: String = "Update New Version",
    var cancelButtonTitle: String = "Cancel",
    var updateButtonTitleColor: Color? = null,
    var submitButtonColor: Color? = null,
    var cancelButtonColor: Color? = null,
    var cancelButtonCornerColor: Color? = null,
    var submitButtonCornerRadius: Dp? = null,
    var cancelButtonCornerRadius: Dp? = null,
    var updateButtonBorderColor: Color? = null,
    var submitButtonLayoutModifier:   Modifier? = null,
    var cancelButtonLayoutModifier:   Modifier? = null,
    var versionTitle: String = "Up to 12.349 version Apr 2024.",
    var versionTitleColor: Color? = null,
    var versionTitleLayoutModifier:   Modifier? = null,
    var imageView: @Composable ((String) -> Unit)? = null,
    var closeButtonView: @Composable ((Int, onClick: () -> Unit ) -> Unit)? = null,

    var lineView: @Composable (() -> Unit)? = null,
    var headerTitleView: @Composable ((String) -> Unit)? = null,
    var descriptionTitleView: @Composable ((String) -> Unit)? = null,
    var versionTitleView: @Composable (() -> Unit)? = null,
    var submitButtonView: (@Composable (libraryOnClick: () -> Unit) -> Unit)? = null,
    var cancelButtonView: (@Composable (libraryOnClick: () -> Unit) -> Unit)? = null,
)
