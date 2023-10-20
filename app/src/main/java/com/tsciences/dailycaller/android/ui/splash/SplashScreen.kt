package com.tsciences.dailycaller.android.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import com.tsciences.dailycaller.android.R
import com.tsciences.dailycaller.android.core.theme.gray
import com.tsciences.dailycaller.android.ui.commonComponents.DailyCallerScaffold

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SplashScreen() {
    DailyCallerScaffold(containerColor = gray) {
        Image(
            painter = painterResource(id = R.drawable.splash_screen_bg),
            contentDescription = "options",
            modifier = Modifier.fillMaxSize()
        )
    }
}

