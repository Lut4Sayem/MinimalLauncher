package com.example.minimallauncher.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minimallauncher.model.AppInfo

@Composable
fun AppDrawerScreen(
    apps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit
) {
    var query by rememberSaveable {
        mutableStateOf("")
    }
    val filteredApps by remember(apps) {
        derivedStateOf {
            if (query.isBlank()) {
                apps
            } else {
                apps.filter { app ->
                    app.name.contains(query.trim(), ignoreCase = true)
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Color.Black.copy(alpha = 0.35f)
            )
            .padding(
                start = 32.dp,
                top = 60.dp,
                end = 32.dp
            )
    ) {

        // App drawer heading
        Text(
            text = "ALL APPS",
            fontSize = 12.sp,
            color = Color.White.copy(alpha = 0.7f)
        )

        AppSearchField(
            query = query,
            onQueryChange = { query = it },
            onClear = { query = "" },
            modifier = Modifier.padding(top = 18.dp)
        )

        if (filteredApps.isEmpty()) {
            Text(
                text = "No apps found",
                fontSize = 15.sp,
                color = Color.White.copy(alpha = 0.6f),
                modifier = Modifier.padding(top = 32.dp)
            )
        } else {
            LazyColumn(
                modifier = Modifier.padding(top = 12.dp)
            ) {
                items(
                    items = filteredApps,
                    key = { app -> app.packageName }
                ) { app ->

                    Text(
                        text = app.name,
                        fontSize = 18.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                onAppClick(app)
                            }
                            .padding(vertical = 12.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun AppSearchField(
    query: String,
    onQueryChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                value = query,
                onValueChange = onQueryChange,
                singleLine = true,
                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 17.sp
                ),
                cursorBrush = SolidColor(Color.White),
                decorationBox = { innerTextField ->
                    if (query.isEmpty()) {
                        Text(
                            text = "Search apps",
                            fontSize = 17.sp,
                            color = Color.White.copy(alpha = 0.55f)
                        )
                    }
                    innerTextField()
                },
                modifier = Modifier.weight(1f)
            )

            if (query.isNotEmpty()) {
                Text(
                    text = "CLEAR",
                    fontSize = 11.sp,
                    color = Color.White.copy(alpha = 0.7f),
                    modifier = Modifier
                        .clickable(onClick = onClear)
                        .padding(start = 16.dp, top = 4.dp, bottom = 4.dp)
                )
            } else {
                Spacer(modifier = Modifier.padding(end = 1.dp))
            }
        }

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
                .background(Color.White.copy(alpha = 0.35f))
                .padding(top = 1.dp)
        )
    }
}
