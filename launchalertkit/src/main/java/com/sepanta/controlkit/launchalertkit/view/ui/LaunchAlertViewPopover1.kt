package com.sepanta.controlkit.launchalertkit.view.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.sepanta.controlkit.launchalertkit.R
import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.theme.Black100
import com.sepanta.controlkit.launchalertkit.theme.Green20
import com.sepanta.controlkit.launchalertkit.theme.Orange80
import com.sepanta.controlkit.launchalertkit.theme.Typography
import com.sepanta.controlkit.launchalertkit.util.Utils.openLink
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewConfig
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewContract
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModel

class LaunchAlertViewPopover1 : LaunchAlertViewContract {

    @Composable
    override fun ShowView(
        config: LaunchAlertViewConfig, response: CheckUpdateResponse,
        viewModel: LaunchAlertViewModel

    ) {
        val openDialog = viewModel.openDialog.collectAsState()
        if (!openDialog.value) return
        Dialog(
            onDismissRequest = { viewModel.dismissDialog() },
            properties = DialogProperties(
                dismissOnClickOutside = true,
                dismissOnBackPress = false,
            )
        ) {
            Column(
                modifier = config.popupViewLayoutModifier ?: Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.9f)
                    .background(Transparent),
                horizontalAlignment = Alignment.CenterHorizontally

            ) {
                Surface(
                    modifier = config.popupViewLayoutModifier ?: Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(config.popupViewCornerRadius ?: 15.dp),
                    color = config.popupViewBackGroundColor ?: Green20,

                    ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CloseButton(config, viewModel)
                        ImageView(config, response)
                        HeaderTitle(config, response)
                        DescriptionTitle(config, response)
                    }
                }
                OverlapButtonLayout(config, response, viewModel)


            }

        }

    }

    @Composable
    private fun OverlapButtonLayout(
        config: LaunchAlertViewConfig,
        response: CheckUpdateResponse,
        viewModel: LaunchAlertViewModel
    ) {
        Column(
            modifier = config.popupViewLayoutModifier ?: Modifier
                .wrapContentSize()
                .offset(y = (-28).dp),

            ) {
            ButtonSubmit(config, response, viewModel)

        }
    }


    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun CloseButton(config: LaunchAlertViewConfig, viewModel: LaunchAlertViewModel) {

        val onClickAction: () -> Unit = {
            viewModel.dismissDialog()
        }

        Surface(
            modifier = config.closeImageLayoutModifier ?: Modifier
                .padding(top = 10.dp, end = 10.dp)
                .wrapContentSize(),
            color = Transparent,
        ) {
            config.closeButtonView?.let { imageView ->
                imageView(config.closeImageDrawble ?: R.drawable.close, onClickAction)

            } ?: Row(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.weight(1f))

                Surface(
                    modifier = Modifier
                        .width(28.dp)
                        .height(28.dp),
                    color = Transparent,
                    onClick = {
                        onClickAction()
                    }
                ) {
                    Icon(
                        modifier = Modifier
                            .width(28.dp)
                            .height(28.dp),
                        painter = painterResource(
                            id = config.closeImageDrawble ?: R.drawable.close
                        ),
                        contentDescription = null,
                        tint = config.closeImageColor ?: Orange80
                    )
                }
            }

        }

    }

    @Composable
    private fun ImageView(config: LaunchAlertViewConfig, response: CheckUpdateResponse) {
        Surface(
            modifier = config.imageLayoutModifier ?: Modifier
                .padding(top = 10.dp)
                .wrapContentWidth()
                .height(50.dp),
            color = Transparent,

            ) {
            config.imageView?.let { imageView ->
                response.iconUrl?.let { imageView(it) }
            } ?: if (config.imageDrawble != null) Image(
                painter = painterResource(
                    id = config.imageDrawble!!
                ),
                contentScale = config.contentScaleImageDrawble ?: ContentScale.Fit,
                contentDescription = null,
            ) else AsyncImage(
                model = response.iconUrl,
                contentDescription = null,
                placeholder = if (config.placeholderImageDrawble == null) null else painterResource(
                    config.placeholderImageDrawble!!
                ),
                contentScale = config.contentScaleImageDrawble ?: ContentScale.Fit,
                error = painterResource(
                    id = config.errorImageDrawble ?: R.drawable.seting
                ),
            )

        }

    }

    @Composable
    private fun HeaderTitle(
        config: LaunchAlertViewConfig,
        response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.headerTitleLayoutModifier ?: Modifier
                .padding(top = 30.dp, end = 5.dp, start = 5.dp)
                .wrapContentSize(),
            color = Transparent,

            ) {

            config.headerTitleView?.let { textView ->
                textView((response.title ?: config.headerTitle))
            } ?: Row(verticalAlignment = Alignment.CenterVertically) {
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Black100
                )
                Text(
                    modifier = Modifier
                        .wrapContentSize()
                        .padding(end = 10.dp, start = 10.dp),
                    text = response.title ?: config.headerTitle,
                    style = Typography.titleLarge,
                    color = config.headerTitleColor ?: Typography.titleLarge.color

                )
                HorizontalDivider(
                    modifier = Modifier.weight(1f),
                    thickness = 1.dp,
                    color = Black100
                )

            }

        }

    }

    @Composable
    private fun DescriptionTitle(
        config: LaunchAlertViewConfig,
        response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.descriptionTitleLayoutModifier ?: Modifier
                .padding(top = 20.dp, end = 15.dp, start = 15.dp, bottom = 80.dp)
                .wrapContentSize(),
            color = Transparent,

            ) {
            config.descriptionTitleView?.let { textView ->
                textView((response.description ?: config.descriptionTitle))
            } ?: Text(
                text = response.description
                    ?: config.descriptionTitle,
                style = Typography.titleMedium,
                color = config.descriptionTitleColor ?: Typography.titleMedium.color

            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ButtonSubmit(
        config: LaunchAlertViewConfig,
        response: CheckUpdateResponse,
        viewModel: LaunchAlertViewModel
    ) {
        val uriHandler = LocalUriHandler.current

        val onClickAction: () -> Unit = {
            openLink(
                response.linkUrl,
                uriHandler
            )
            viewModel.submitDialog()

        }

        config.submitButtonView?.let { button ->
            button(onClickAction)
        } ?: Button(
            onClick = onClickAction,
            shape = RoundedCornerShape(config.submitButtonCornerRadius ?: 18.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = config.submitButtonColor ?: Orange80
            ),
            modifier = Modifier.size(width = 182.dp, height = 56.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp)
        ) {
            Text(
                text = response.buttonTitle ?: config.submitButtonTitle,
                style = Typography.titleMedium
            )
        }


    }


}