package com.sepanta.controlkit.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.sepanta.controlkit.example.ui.theme.TestLaunchAlrertTheme
import com.sepanta.controlkit.launchalertkit.config.LaunchAlertServiceConfig
import com.sepanta.controlkit.launchalertkit.launchAlertKitHost
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewConfig
import com.sepanta.controlkit.launchalertkit.view.config.LaunchAlertViewStyle


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TestLaunchAlrertTheme {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 50.dp),
                    color = Color.White
                ) {

                    val launchAlertKit = launchAlertKitHost(
                        LaunchAlertServiceConfig(
                            version = "1",
                            appId = "9fee1663-e80e-46ad-8cd9-357263375a9c",
                            viewConfig = LaunchAlertViewConfig(
                                LaunchAlertViewStyle.Popover4
                            )
                        ),
                        onDismiss = {

                        }
                    )

                    launchAlertKit.showView()
                }
            }
        }
    }
}
