package com.example.minimallauncher

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.platform.LocalContext
import com.example.minimallauncher.data.AppRepository
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.clickable
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            LauncherHomeScreen()
        }
    }
}

@Composable
fun LauncherHomeScreen() {
    val context = LocalContext.current

    val appRepository = remember {
        AppRepository(context)
    }

    val installedApps = remember {
        appRepository.getInstalledApps()
    }

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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(
                start = 32.dp,
                top = 70.dp,
                end = 32.dp
            )
    ) {

        Row {

            Text(
                text = day,
                fontSize = 90.sp,
                color = Color.Black
            )

            Spacer(
                modifier = Modifier.width(14.dp)
            )

            Column(
                modifier = Modifier.padding(top = 17.dp)
            ) {

                Text(
                    text = monthYear,
                    fontSize = 24.sp,
                    color = Color.Black
                )

                Text(
                    text = weekDay,
                    fontSize = 13.sp,
                    color = Color.Black
                )
            }
        }

        Spacer(
            modifier = Modifier.height(10.dp)
        )

        Text(
            text = time,
            fontSize = 18.sp,
            color = Color.Black
        )
        Spacer(
            modifier = Modifier.height(30.dp)
        )

        Text(
            text = "APPS",
            fontSize = 12.sp,
            color = Color.Gray
        )

        Spacer(
            modifier = Modifier.height(12.dp)
        )

        LazyColumn {

            items(installedApps) { app ->

                Text(
                    text = app.name,
                    fontSize = 18.sp,
                    color = Color.Black,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {

                            val launchIntent =
                                context.packageManager
                                    .getLaunchIntentForPackage(app.packageName)

                            if (launchIntent != null) {
                                context.startActivity(launchIntent)
                            }
                        }
                        .padding(vertical = 10.dp)
                )
            }
        }
    }
}