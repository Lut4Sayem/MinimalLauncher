package com.example.minimallauncher.data

import android.content.Context
import android.content.Intent
import com.example.minimallauncher.model.AppInfo

class AppRepository(
    private val context: Context
) {

    fun getInstalledApps(): List<AppInfo> {

        val packageManager = context.packageManager

        val intent = Intent(
            Intent.ACTION_MAIN,
            null
        ).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        val resolvedApps = packageManager.queryIntentActivities(
            intent,
            0
        )

        return resolvedApps
            .map { resolveInfo ->

                AppInfo(
                    name = resolveInfo
                        .loadLabel(packageManager)
                        .toString(),

                    packageName = resolveInfo
                        .activityInfo
                        .packageName
                )
            }
            .filter { app ->
                app.packageName != context.packageName
            }
            .distinctBy { app ->
                app.packageName
            }
            .sortedBy { app ->
                app.name.lowercase()
            }
    }
}