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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.minimallauncher.data.FavoritesRepository
import kotlinx.coroutines.launch


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
    val favoritesRepository = remember {
        FavoritesRepository(context)
    }
    val favoritePackageNames by favoritesRepository.favoritePackageNames
        .collectAsStateWithLifecycle(initialValue = emptyList())
    val favoritePackageNameSet = remember(favoritePackageNames) {
        favoritePackageNames.toSet()
    }
    val favoriteApps = remember(installedApps, favoritePackageNames) {
        val installedAppsByPackage = installedApps.associateBy { app ->
            app.packageName
        }
        favoritePackageNames.mapNotNull { packageName ->
            installedAppsByPackage[packageName]
        }
    }
    val coroutineScope = rememberCoroutineScope()

    if (showAppDrawer) {

        AppDrawerScreen(
            apps = installedApps,
            favoritePackageNames = favoritePackageNameSet,
            onAppClick = { app ->

                val launchIntent =
                    context.packageManager
                        .getLaunchIntentForPackage(app.packageName)

                if (launchIntent != null) {
                    context.startActivity(launchIntent)
                }
            },
            onFavoriteChange = { app, shouldBeFavorite ->
                coroutineScope.launch {
                    if (shouldBeFavorite) {
                        favoritesRepository.addFavorite(app.packageName)
                    } else {
                        favoritesRepository.removeFavorite(app.packageName)
                    }
                }
            }
        )

    } else {

        HomeScreen(
            favoriteApps = favoriteApps,

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
