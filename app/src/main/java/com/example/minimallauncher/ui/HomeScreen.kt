package com.example.minimallauncher.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.minimallauncher.model.AppInfo
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
@Composable
fun HomeScreen(
    favoriteApps: List<AppInfo>,
    onAppClick: (AppInfo) -> Unit,
    onSwipeUp: () -> Unit
) {

    var currentTime by remember {
        mutableStateOf(LocalDateTime.now())
    }

    LaunchedEffect(Unit) {
        while (true) {
            currentTime = LocalDateTime.now()
            delay(1000)
        }
    }

    val day = currentTime.format(
        DateTimeFormatter.ofPattern("dd")
    )

    val monthYear = currentTime.format(
        DateTimeFormatter.ofPattern("MMMM yyyy")
    )

    val weekDay = currentTime.format(
        DateTimeFormatter.ofPattern("EEEE")
    ).uppercase()

    val time = currentTime.format(
        DateTimeFormatter.ofPattern("HH:mm")
    )
    val textShadow = Shadow(
        color = Color.Black.copy(alpha = 0.35f),
        offset = Offset(1f, 2f),
        blurRadius = 6f
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {

                var totalDrag = 0f

                detectVerticalDragGestures(
                    onVerticalDrag = { _, dragAmount ->
                        totalDrag += dragAmount
                    },

                    onDragEnd = {
                        if (totalDrag < -100f) {
                            onSwipeUp()
                        }

                        totalDrag = 0f
                    }
                )
            }
            .padding(
                start = 32.dp,
                top = 70.dp,
                end = 32.dp
            )
    ) {

        Row {

            Text(
                text = day,
                fontSize = 96.sp,
                fontWeight = FontWeight.Light,
                color = Color.White,
                style = TextStyle(
                    shadow = textShadow
                )
            )

            Spacer(
                modifier = Modifier.width(16.dp)
            )

            Column(
                modifier = Modifier.padding(top = 20.dp)
            ) {

                Text(
                    text = monthYear.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    style = TextStyle(
                        shadow = textShadow
                    )

                )

                Text(
                    text = weekDay,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White.copy(alpha = 0.75f),
                    style = TextStyle(
                        shadow = textShadow
                    )
                )
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = time,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = Color.White.copy(alpha = 0.85f),
            style = TextStyle(
                shadow = textShadow
            )
        )

        // Takes up the empty middle area of the screen.
        // This pushes favorite apps toward the bottom.
        Spacer(
            modifier = Modifier.weight(1f)
        )

        Column(
            modifier = Modifier.padding(bottom = 50.dp)
        ) {

            favoriteApps.forEach { app ->

                FavoriteAppItem(
                    app = app,
                    onClick = {
                        onAppClick(app)
                    }
                )
            }
        }
    }
}


// This is a separate reusable Composable.
// It is NOT inside HomeScreen().
@Composable
fun FavoriteAppItem(
    app: AppInfo,
    onClick: () -> Unit
) {

    Text(
        text = app.name,
        fontSize = 16.sp,
        fontWeight = FontWeight.Medium,
        color = Color.White.copy(alpha = 0.9f),
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }
            .padding(vertical = 8.dp)
    )
}