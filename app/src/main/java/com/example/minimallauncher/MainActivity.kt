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
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            onSwipeUp = {
                showAppDrawer = true
            }
        )
    }
}