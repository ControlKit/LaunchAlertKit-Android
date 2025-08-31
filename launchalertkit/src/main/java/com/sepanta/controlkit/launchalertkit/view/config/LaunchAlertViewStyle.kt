package com.sepanta.controlkit.launchalertkit.view.config

import com.sepanta.controlkit.launchalertkit.view.ui.LaunchAlertViewFullScreen1
import com.sepanta.controlkit.launchalertkit.view.ui.LaunchAlertViewPopover1
import com.sepanta.controlkit.launchalertkit.view.ui.LaunchAlertViewPopover2
import com.sepanta.controlkit.launchalertkit.view.ui.LaunchAlertViewPopover3
import com.sepanta.controlkit.launchalertkit.view.ui.LaunchAlertViewPopover4


enum class LaunchAlertViewStyle {
    FullScreen1,
    Popover1,
    Popover2,
    Popover3,
    Popover4;
    companion object {
        fun checkViewStyle(style:LaunchAlertViewStyle): LaunchAlertViewContract {
            return when (style) {
                FullScreen1 -> LaunchAlertViewFullScreen1()
                Popover1 -> LaunchAlertViewPopover1()
                Popover2 -> LaunchAlertViewPopover2()
                Popover3 -> LaunchAlertViewPopover3()
                Popover4 -> LaunchAlertViewPopover4()
            }

        }

    }

}
