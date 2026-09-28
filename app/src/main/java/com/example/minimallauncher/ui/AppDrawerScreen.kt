package com.example.minimallauncher.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minimallauncher.model.AppInfo

@Composable
fun AppDrawerScreen(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 32.dp,
                top = 60.dp,
                end = 32.dp
            )
    ) {

        Text(
            text = "ALL APPS",
            fontSize = 14.sp
        )

        LazyColumn(
            modifier = Modifier.padding(top = 20.dp)
        ) {

            items(apps) { app ->

                Text(
                    text = app.name,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            onAppClick(app)
                        }
                        .padding(vertical = 14.dp)
                )
            }
        }
    }
}