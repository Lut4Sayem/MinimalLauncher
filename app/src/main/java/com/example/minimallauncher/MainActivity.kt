package com.example.minimallauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.example.minimallauncher.data.AppRepository
import com.example.minimallauncher.ui.HomeScreen
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.minimallauncher.ui.AppDrawerScreen
import android.graphics.Color
import androidx.activity.compose.BackHandler


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setBackgroundDrawableResource(android.R.color.transparent)

        setContent {
            Launcher()
        }
    }
}

@Composable
fun Launcher() {

    val context = LocalContext.current
    var showAppDrawer by remember {
        mutableStateOf(false)
    }
    BackHandler(
        enabled = showAppDrawer
    ) {
        showAppDrawer = false
    }

    val appRepository = remember {
        AppRepository(context)
    }

    val installedApps = remember {
        appRepository.getInstalledApps()
    }

    if (showAppDrawer) {

        AppDrawerScreen(
            apps = installedApps,
            onAppClick = { app ->

                val launchIntent =
                    context.packageManager
                        .getLaunchIntentForPackage(app.packageName)

                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                }
            }
        )

    } else {

        HomeScreen(
            favoriteApps = installedApps.take(5),

            onAppClick = { app ->

                val launchIntent =
                    context.packageManager
                        .getLaunchIntentForPackage(app.packageName)

                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                }
            },

            onSwipeUp = {
                showAppDrawer = true
            }
        )
    }
}