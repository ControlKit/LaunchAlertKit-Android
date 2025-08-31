package com.sepanta.controlkit.launchalertkit.view.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.sepanta.controlkit.launchalertkit.service.model.CheckUpdateResponse
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewConfig
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewContract
import com.sepanta.controlkit.launchalertkit.R
import com.sepanta.controlkit.launchalertkit.config.utils.openLink
import com.sepanta.controlkit.launchalertkit.theme.Black100
import com.sepanta.controlkit.launchalertkit.theme.Orange80
import com.sepanta.controlkit.launchalertkit.theme.Typography
import com.sepanta.controlkit.launchalertkit.theme.White100
import com.sepanta.controlkit.launchalertkit.theme.Yellow80
import com.sepanta.controlkit.launchalertkit.view.viewmodel.LaunchAlertViewModel

class LaunchAlertViewPopover3 : LaunchAlertViewContract {

    @Composable
    override fun ShowView(
        config: LaunchAlertViewConfig,
        response: CheckUpdateResponse,
        viewModel: LaunchAlertViewModel
    ) {
        val openDialog = viewModel.openDialog.collectAsState()
        if (!openDialog.value) return
        Dialog(
            onDismissRequest = { viewModel.dismissDialog() },
        ) {
            Box(
                modifier = config.popupViewLayoutModifier ?: Modifier
                    .wrapContentHeight()
                    .fillMaxWidth(0.9f)
                    .background(Color.Transparent),

                ) {
                Surface(
                    modifier = config.popupViewLayoutModifier ?: Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomCenter),
                    shape = RoundedCornerShape(config.popupViewCornerRadius ?: 15.dp),
                    color = config.popupViewBackGroundColor ?: White100
                ) {

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        HeaderTitle(config, response)
                        DescriptionTitle(config, response)
                        ButtonSubmit(config, response, viewModel)
                        ButtonCancel(config, viewModel, response)
                    }
                }
                OverlapImageLayout(config, Modifier.align(Alignment.TopCenter), response)

            }


        }

    }

    @Composable
    private fun OverlapImageLayout(
        config: LaunchAlertViewConfig, modifier: Modifier, response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.imageLayoutModifier ?: modifier
                .height(120.dp)
                .offset(y = (-28).dp),
            color = Color.Transparent,

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
                    id = config.errorImageDrawble ?: R.drawable.background3
                ),
            )


        }

    }

    @Composable
    private fun HeaderTitle(
        config: LaunchAlertViewConfig, response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.headerTitleLayoutModifier ?: Modifier
                .padding(
                    top = 130.dp, end = 15.dp, start = 15.dp
                )
                .wrapContentSize(),
            color = Color.Transparent,

            ) {
            config.headerTitleView?.let { textView ->
                textView((response.title ?: config.headerTitle))
            } ?: Text(
                text = response.title ?: config.headerTitle,
                style = Typography.titleLarge,
                color = config.headerTitleColor ?: Typography.titleLarge.color

            )
        }

    }

    @Composable
    private fun DescriptionTitle(
        config: LaunchAlertViewConfig, response: CheckUpdateResponse
    ) {
        Surface(
            modifier = config.descriptionTitleLayoutModifier ?: Modifier
                .padding(
                    top = 20.dp, end = 15.dp, start = 15.dp
                )
                .wrapContentSize(),
            color = Color.Transparent,

            ) {
            config.descriptionTitleView?.let { textView ->
                textView((response.description ?: config.descriptionTitle))
            } ?: Text(
                text = response.description ?: config.descriptionTitle,
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
            openLink(response.linkUrl, uriHandler)
            viewModel.submitDialog()
        }

        config.submitButtonView?.let { button ->
            button(onClickAction)
        } ?: Button(
            onClick = onClickAction,
            shape = RoundedCornerShape(config.submitButtonCornerRadius ?: 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = config.submitButtonColor ?: Orange80
            ),
            modifier = Modifier
                .padding(top = 42.dp)
                .height(42.dp)
                .fillMaxWidth()
                .padding(end = 22.dp, start = 22.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 5.dp)
        ) {
            Text(
                text = response.buttonTitle ?: config.submitButtonTitle,
                style = Typography.titleMedium
            )
        }

    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun ButtonCancel(
        config: LaunchAlertViewConfig,
        viewModel: LaunchAlertViewModel,
        response: CheckUpdateResponse
    ) {
        val onClickAction: () -> Unit = {
            viewModel.dismissDialog()
        }
        config.cancelButtonView?.let { button ->
            button(onClickAction)
        } ?:

        Button(
            onClick = onClickAction,
            shape = RoundedCornerShape(config.cancelButtonCornerRadius ?: 20.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = config.cancelButtonColor ?:  Color.Transparent
            ),
            border = BorderStroke(1.dp, Orange80),
            modifier = Modifier
                .padding(top = 5.dp, bottom = 40.dp)
                .height(42.dp)
                .fillMaxWidth()
                .padding(end = 22.dp, start = 22.dp),

            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
        ) {
            Text(
                text = response.cancelButtonTitle ?: config.cancelButtonTitle,
                style = Typography.titleMedium,
                color = Orange80
            )
        }

    }

}